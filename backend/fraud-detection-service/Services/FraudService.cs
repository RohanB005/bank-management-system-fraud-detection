using FraudDetectionService.Data;
using FraudDetectionService.DTOs;
using FraudDetectionService.Entities;
using FraudDetectionService.Enums;
using FraudDetectionService.Services.Interfaces;
using FraudDetectionService.Helpers;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;

namespace FraudDetectionService.Services
{
    public class FraudService : IFraudService
    {
        private readonly FraudDbContext _context;
        private readonly IAIExplanationService _aiExplanationService;
        private readonly ILogger<FraudService> _logger;

        public FraudService(
            FraudDbContext context,
            IAIExplanationService aiExplanationService,
            ILogger<FraudService> logger)
        {
            _context = context;
            _aiExplanationService = aiExplanationService;
            _logger = logger;
        }

        public async Task<FraudCheckResponse> CheckFraudAsync(FraudCheckRequest request)
        {
            _logger.LogInformation(
                "Checking Fraud for Customer {CustomerId}, Amount {Amount}",
                request.CustomerId,
                request.TransactionAmount);

            var previousTransaction = await _context.FraudLogs
                .Where(x => x.CustomerId == request.CustomerId)
                .OrderByDescending(x => x.CreatedAt)
                .FirstOrDefaultAsync();

            string? previousIpAddress = previousTransaction?.ClientIpAddress;
            string? previousCity = previousTransaction?.CurrentTransactionCity;

            var riskResult = RiskScoreCalculator.CalculateRiskScore(
                request.TransactionAmount,
                previousIpAddress,
                request.ClientIpAddress,
                previousCity,
                request.CurrentTransactionCity
            );

            int riskScore = riskResult.Score;
            bool isFraud = riskScore >= 80;
            string? aiExplanation = null;

            if (isFraud)
            {
                var tempLog = new FraudLog
                {
                    TransactionId = request.TransactionId,
                    CustomerId = request.CustomerId,
                    AccountId = request.AccountId,
                    TransactionAmount = request.TransactionAmount,
                    TransactionType = request.TransactionType,
                    ClientIpAddress = request.ClientIpAddress,
                    CurrentTransactionCity = request.CurrentTransactionCity,
                    PreviousIpAddress = previousIpAddress,
                    PreviousTransactionCity = previousCity,
                    RiskScore = riskScore
                };

                try
                {
                    aiExplanation = await _aiExplanationService.GenerateFraudExplanationAsync(tempLog);
                }
                catch (Exception ex)
                {
                    _logger.LogError(ex, "Gemini API failed for Fraud check");
                    aiExplanation = "AI explanation unavailable due to temporary service error.";
                }
            }

            // Note: Normal/Safe transactions are NEVER stored in FraudLog per business requirements.
            return new FraudCheckResponse
            {
                IsFraud = isFraud,
                RiskScore = riskScore,
                Message = isFraud ? "Suspicious Transaction Detected" : "Transaction Safe",
                AIExplanation = aiExplanation
            };
        }

        public async Task RecordDecisionAsync(RecordDecisionRequest request)
        {
            _logger.LogInformation(
                "Recording Customer Fraud Decision: {Decision} for Customer {CustomerId}",
                request.Decision,
                request.CustomerId);

            var previousTransaction = await _context.FraudLogs
                .Where(x => x.CustomerId == request.CustomerId)
                .OrderByDescending(x => x.CreatedAt)
                .FirstOrDefaultAsync();

            bool isAllowed = request.Decision.Equals("Allowed", StringComparison.OrdinalIgnoreCase);

            var fraudLog = new FraudLog
            {
                TransactionId = 0,
                CustomerId = request.CustomerId,
                AccountId = request.AccountId,
                TransactionAmount = request.TransactionAmount,
                TransactionType = request.TransactionType,
                ClientIpAddress = request.ClientIpAddress,
                CurrentTransactionCity = request.CurrentTransactionCity,
                PreviousIpAddress = previousTransaction?.ClientIpAddress,
                PreviousTransactionCity = previousTransaction?.CurrentTransactionCity,
                RiskScore = request.RiskScore,
                Status = FraudStatus.Flagged,
                AlertMessage = "Suspicious Transaction Flagged",
                CustomerDecision = isAllowed ? CustomerDecision.Allowed : CustomerDecision.Blocked,
                ActionTaken = isAllowed ? ActionTaken.Allowed : ActionTaken.Blocked,
                Reason = request.Reason ?? (isAllowed ? "Approved by customer security verification" : "Blocked by customer security verification"),
                AIExplanation = request.AIExplanation,
                CreatedAt = DateTime.UtcNow,
                AIProcessedAt = DateTime.UtcNow
            };

            _context.FraudLogs.Add(fraudLog);
            await _context.SaveChangesAsync();

            _logger.LogInformation("Recorded FraudLog ID {FraudId} with Decision {Decision}", fraudLog.FraudId, request.Decision);
        }
    }
}