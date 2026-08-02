using FraudDetectionService.Data;
using FraudDetectionService.DTOs;
using FraudDetectionService.Entities;
using FraudDetectionService.Enums;
using FraudDetectionService.Services.Interfaces;
using FraudDetectionService.Helpers;
using Microsoft.EntityFrameworkCore;
using FraudDetectionService.Services;
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
            ILogger<FraudService> logger    )
        {
            _context = context;
            _aiExplanationService = aiExplanationService;
            _logger = logger;
        }


        public async Task<FraudCheckResponse> CheckFraudAsync(FraudCheckRequest request)
        {
            _logger.LogInformation(
                "Checking Transaction {TransactionID} for Customer {CustomerId}",
                request.TransactionId,
                request.CustomerId);

            //int riskScore = 0;


            //// Temporary fraud rule
            //if (request.TransactionAmount > 100000)
            //{
            //    riskScore = 90;
            //}
            //else
            //{
            //    riskScore = 20;
            //}


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

            var fraudLog = new FraudLog
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
                RiskScore = riskScore,

                Status = riskScore >= 80
                    ? FraudStatus.Flagged
                    : FraudStatus.Allow,


                AlertMessage = riskScore >= 80
                    ? "High Risk Transaction"
                    : null,


                CustomerDecision = CustomerDecision.Pending,


                Reason = riskResult.Reasons.Count > 0
                ? string.Join(" + ", riskResult.Reasons)
                : null,


                ActionTaken = riskScore >= 80
                    ? ActionTaken.Blocked
                    : ActionTaken.Allowed
            };


            // Generate AI explanation only for high-risk transactions
            if(fraudLog.RiskScore >= 80)
{
                try
                {
                    _logger.LogInformation(
                        "Generating AI explanation for Transaction {TransactionId}",
                        fraudLog.TransactionId);

                    fraudLog.AIExplanation =
                        await _aiExplanationService.GenerateFraudExplanationAsync(fraudLog);

                    fraudLog.AIProcessedAt = DateTime.UtcNow;

                    _logger.LogInformation(
                        "AI explanation generated successfully.");
                }
                catch (Exception ex)
                {
                    _logger.LogError(
                        ex,
                        "Gemini API failed for Transaction {TransactionId}",
                        fraudLog.TransactionId);

                    fraudLog.AIExplanation =
                        "AI explanation unavailable due to temporary service error.";

                    fraudLog.AIProcessedAt = DateTime.UtcNow;
                }
            }

            // Save the fraud log
            _context.FraudLogs.Add(fraudLog);



            _logger.LogInformation(
                "Saving FraudLog for Transaction {TransactionId}",
                 fraudLog.TransactionId);

            await _context.SaveChangesAsync();

            _logger.LogInformation(
                "FraudLog saved successfully.");


            return new FraudCheckResponse
            {
                IsFraud = riskScore >= 80,

                RiskScore = riskScore,

                Message = riskScore >= 80
                    ? "Fraud Detected"
                    : "Transaction Safe",

                AIExplanation = fraudLog.AIExplanation
            };
        }
    }
}