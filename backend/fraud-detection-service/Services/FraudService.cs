using FraudDetectionService.Data;
using FraudDetectionService.DTOs;
using FraudDetectionService.Entities;
using FraudDetectionService.Enums;
using FraudDetectionService.Services.Interfaces;
using FraudDetectionService.Helpers;
using Microsoft.EntityFrameworkCore;

namespace FraudDetectionService.Services
{
    public class FraudService : IFraudService
    {
        private readonly FraudDbContext _context;

        public FraudService(FraudDbContext context)
        {
            _context = context;
        }


        public async Task<FraudCheckResponse> CheckFraudAsync(FraudCheckRequest request)
        {
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


            _context.FraudLogs.Add(fraudLog);

            await _context.SaveChangesAsync();


            return new FraudCheckResponse
            {
                IsFraud = riskScore >= 80,

                RiskScore = riskScore,

                Message = riskScore >= 80
                    ? "Fraud Detected"
                    : "Transaction Safe"
            };
        }
    }
}