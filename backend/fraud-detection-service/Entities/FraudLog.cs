using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using FraudDetectionService.Enums;

namespace FraudDetectionService.Entities
{
    /// <summary>
    /// EF Core entity for the FraudLog table. One row is created per call to
    /// POST /api/fraud/check, and updated in place when the customer responds
    /// via POST /api/fraud/customer-response.
    ///
    /// This entity belongs entirely to the Fraud Service — it never touches
    /// Account or Transaction data owned by the Java microservices; it only
    /// records the IDs (transactionId, customerId, accountId) it was given.
    /// </summary>
    [Table("FraudLog")]
    public class FraudLog
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int FraudId { get; set; }

        /// <summary>The transactionId supplied by the Java Transaction Service.</summary>
        [Required]
        public long TransactionId { get; set; }

        [Required]
        public long CustomerId { get; set; }

        [Required]
        public long AccountId { get; set; }

        [Required]
        [Column(TypeName = "decimal(18,2)")]
        public decimal TransactionAmount { get; set; }

        /// <summary>e.g. "TRANSFER" — mirrors the Java service's TransactionType.</summary>
        [Required]
        [MaxLength(30)]
        public string TransactionType { get; set; } = string.Empty;

        [Required]
        [MaxLength(45)] // 45 chars comfortably covers IPv6 literal length
        public string ClientIpAddress { get; set; } = string.Empty;

        /// <summary>Null when this is the customer's first recorded transaction.</summary>
        [MaxLength(45)]
        public string? PreviousIpAddress { get; set; }

        [Required]
        [MaxLength(100)]
        public string CurrentTransactionCity { get; set; } = string.Empty;

        /// <summary>Null when this is the customer's first recorded transaction.</summary>
        [MaxLength(100)]
        public string? PreviousTransactionCity { get; set; }

        [Required]
        public int RiskScore { get; set; }

        [Required]
        public FraudStatus Status { get; set; }

        /// <summary>The simulated alert text shown to the customer (see Feature 4). Null when Status = Allow.</summary>
        public string? AlertMessage { get; set; }

        [Required]
        public CustomerDecision CustomerDecision { get; set; } = Enums.CustomerDecision.Pending;

        /// <summary>e.g. "High Amount + Location Changed". Null when Status = Allow.</summary>
        [MaxLength(200)]
        public string? Reason { get; set; }

        [Required]
        public ActionTaken ActionTaken { get; set; }

        [Required]
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    }
}
