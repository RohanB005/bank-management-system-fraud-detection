namespace FraudDetectionService.Enums
{
    /// <summary>
    /// The customer's response to a fraud alert (POST /api/fraud/customer-response).
    /// Defaults to Pending until the customer actually replies.
    /// </summary>
    public enum CustomerDecision
    {
        Pending,
        Yes,
        No
    }
}
