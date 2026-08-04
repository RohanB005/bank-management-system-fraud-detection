import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { getAccountsByCustomer, withdrawMoney } from '../api';
import { formatAccountNumber } from '../utils/formatters';
import { ArrowUpRight, AlertCircle, CheckCircle } from 'lucide-react';

export const Withdraw = () => {
  const { user } = useAuth();
  const [accounts, setAccounts] = useState([]);
  const [accountId, setAccountId] = useState('');
  const [amount, setAmount] = useState('');
  const [description, setDescription] = useState('ATM cash withdrawal');
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  // Automatic location detection
  const [city] = useState('Pune');

  useEffect(() => {
    if (user?.customerId) {
      getAccountsByCustomer(user.customerId).then((res) => {
        const list = res.data?.data || [];
        setAccounts(list);
        if (list.length > 0) setAccountId(list[0].accountId);
      });
    }
  }, [user]);

  const handleKeyDown = (e) => {
    // Prevent minus sign, plus sign, and exponent e character
    if (['-', '+', 'e', 'E'].includes(e.key)) {
      e.preventDefault();
    }
  };

  const validate = () => {
    const errs = {};
    if (!accountId) {
      errs.accountId = 'Please select an account.';
    }

    if (!amount) {
      errs.amount = 'Amount is required.';
    } else {
      const num = parseFloat(amount);
      if (isNaN(num) || num <= 0) {
        errs.amount = 'Amount must be greater than zero.';
      }
    }

    if (!description.trim()) {
      errs.description = 'Description / Remarks is required.';
    }

    setErrors(errs);
    return Object.keys(errs).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    if (!validate()) {
      return;
    }

    setLoading(true);

    try {
      const res = await withdrawMoney({
        accountId: parseInt(accountId),
        amount: parseFloat(amount),
        description: description.trim(),
        transactionCity: city
      });
      setSuccess(`Successfully withdrawn ₹${parseFloat(amount).toLocaleString('en-IN', { minimumFractionDigits: 2 })}! Reference: ${res.data?.data?.referenceNumber}`);
      setAmount('');
      // Refresh accounts balance
      if (user?.customerId) {
        const refreshRes = await getAccountsByCustomer(user.customerId);
        setAccounts(refreshRes.data?.data || []);
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Withdrawal failed. Check balance.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="animate-fade-in" style={{ maxWidth: '540px', margin: '0 auto' }}>
      <div className="glass-panel" style={{ padding: '2.5rem' }}>
        <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
          <div style={{ background: 'rgba(244, 63, 94, 0.2)', width: '56px', height: '56px', borderRadius: '16px', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 1rem' }}>
            <ArrowUpRight size={32} color="#f43f5e" />
          </div>
          <h1 style={{ fontSize: '1.75rem', fontWeight: 700, color: '#fff' }}>Withdraw Money</h1>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Debit funds securely from your active account</p>
        </div>

        {error && (
          <div style={{ background: 'rgba(244, 63, 94, 0.15)', border: '1px solid var(--accent-rose)', color: '#fca5a5', padding: '0.75rem 1rem', borderRadius: 'var(--radius-md)', marginBottom: '1.5rem', display: 'flex', alignItems: 'center', gap: '0.6rem' }}>
            <AlertCircle size={18} /> {error}
          </div>
        )}

        {success && (
          <div style={{ background: 'rgba(16, 185, 129, 0.15)', border: '1px solid var(--accent-emerald)', color: '#6ee7b7', padding: '0.75rem 1rem', borderRadius: 'var(--radius-md)', marginBottom: '1.5rem', display: 'flex', alignItems: 'center', gap: '0.6rem' }}>
            <CheckCircle size={18} /> {success}
          </div>
        )}

        <form onSubmit={handleSubmit} noValidate>
          <div className="form-group" style={{ marginBottom: '1.25rem' }}>
            <label className="form-label">Select Source Account</label>
            <select
              className={`form-select ${errors.accountId ? 'form-input-error' : ''}`}
              value={accountId}
              onChange={(e) => {
                setAccountId(e.target.value);
                if (errors.accountId) setErrors({ ...errors, accountId: null });
              }}
            >
              <option value="">-- Select Account --</option>
              {accounts.map((acc) => (
                <option key={acc.accountId} value={acc.accountId}>
                  {acc.accountType} - {formatAccountNumber(acc.accountNumber)} (Available: ₹{parseFloat(acc.balance).toLocaleString('en-IN', { minimumFractionDigits: 2 })})
                </option>
              ))}
            </select>
            {errors.accountId && <div className="error-text"><AlertCircle size={13} /> {errors.accountId}</div>}
          </div>

          <div className="form-group" style={{ marginBottom: '1.25rem' }}>
            <label className="form-label">Withdrawal Amount (₹)</label>
            <input
              type="number"
              step="0.01"
              min="0.01"
              onKeyDown={handleKeyDown}
              className={`form-input ${errors.amount ? 'form-input-error' : ''}`}
              placeholder="e.g. 2000"
              value={amount}
              onChange={(e) => {
                const val = e.target.value;
                if (parseFloat(val) < 0) return;
                setAmount(val);
                if (errors.amount) setErrors({ ...errors, amount: null });
              }}
            />
            {errors.amount && <div className="error-text"><AlertCircle size={13} /> {errors.amount}</div>}
          </div>

          <div className="form-group" style={{ marginBottom: '1.5rem' }}>
            <label className="form-label">Description / Remarks</label>
            <input
              type="text"
              className={`form-input ${errors.description ? 'form-input-error' : ''}`}
              value={description}
              onChange={(e) => {
                setDescription(e.target.value);
                if (errors.description) setErrors({ ...errors, description: null });
              }}
            />
            {errors.description && <div className="error-text"><AlertCircle size={13} /> {errors.description}</div>}
          </div>

          <button className="btn-danger" type="submit" style={{ width: '100%', padding: '0.85rem' }} disabled={loading}>
            {loading ? 'Processing Withdrawal...' : 'Confirm Withdrawal'}
          </button>
        </form>
      </div>
    </div>
  );
};
