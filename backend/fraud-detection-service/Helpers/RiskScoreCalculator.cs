namespace FraudDetectionService.Helpers
{
    public static class RiskScoreCalculator
    {

            public static RiskCalculationResult CalculateRiskScore(
                decimal transactionAmount,
                string? previousIpAddress,
                string currentIpAddress,
                string? previousCity,
                string currentCity)
            {
            int riskScore = 0;

            List<string> reasons = new();


            // Rule 1: High amount
            if (transactionAmount > 100000)
            {
                riskScore += 40;
                reasons.Add("High Amount");
            }


            // Rule 2: IP changed
            if (!string.IsNullOrEmpty(previousIpAddress)
                && previousIpAddress != currentIpAddress)
            {
                riskScore += 25;
                reasons.Add("IP Address Changed");
            }


            // Rule 3: City changed
            if (!string.IsNullOrEmpty(previousCity)
                && previousCity != currentCity)
            {
                riskScore += 25;
                reasons.Add("Location Changed");
            }


            return new RiskCalculationResult
            {
                Score = riskScore,
                Reasons = reasons
            };
        }
    }
}
