import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { getAccountsByCustomer, createAccount, closeAccount } from '../api';
import { formatAccountId, formatAccountNumber } from '../utils/formatters';
import { PlusCircle, AlertCircle, CheckCircle, XCircle } from 'lucide-react';

export const Accounts = () => {
  const { user } = useAuth();
  const [accounts, setAccounts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const [newAccData, setNewAccData] = useState({
    accountType: 'Savings',
    branchName: 'Main City Branch',
    ifscCode: 'BKID000101'
  });
  const [modalErrors, setModalErrors] = useState({});

  const fetchAccounts = async () => {
    if (!user?.customerId) return;
    try {
      const res = await getAccountsByCustomer(user.customerId);
      setAccounts(res.data?.data || []);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAccounts();
  }, [user]);

  const validateModal = () => {
    const errs = {};
    if (!newAccData.branchName.trim()) {
      errs.branchName = 'Branch Name is required.';
    }

    if (!newAccData.ifscCode.trim()) {
      errs.ifscCode = 'IFSC Code is required.';
    } else if (!/^[A-Z0-9]{11}$/.test(newAccData.ifscCode.trim().toUpperCase())) {
      errs.ifscCode = 'Invalid IFSC Code. Please enter an 11-character code (e.g. BKID000101).';
    }

    setModalErrors(errs);
    return Object.keys(errs).length === 0;
  };

  const handleCreateAccount = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    if (!validateModal()) {
      return;
    }

    try {
      await createAccount({
        customerId: user.customerId,
        accountType: newAccData.accountType,
        branchName: newAccData.branchName.trim(),
        ifscCode: newAccData.ifscCode.trim().toUpperCase()
      });
      setSuccess('Account created successfully!');
      setModalOpen(false);
      fetchAccounts();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to create account.');
    }
  };

  const handleCloseAccount = async (accountId) => {
    if (!window.confirm('Are you sure you want to close this account?')) return;
    setError('');
    setSuccess('');
    try {
      await closeAccount(accountId);
      setSuccess('Account closed successfully.');
      fetchAccounts();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to close account.');
    }
  };

  return (
    <div className="animate-fade-in" style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h1 style={{ fontSize: '1.75rem', fontWeight: 700, color: '#fff' }}>Account Management</h1>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Manage savings, salary, and current accounts</p>
        </div>
        <button className="btn-primary" onClick={() => { setModalErrors({}); setModalOpen(true); }}>
          <PlusCircle size={18} /> Open New Account
        </button>
      </div>

      {error && (
        <div style={{ background: 'rgba(244, 63, 94, 0.15)', border: '1px solid var(--accent-rose)', color: '#fca5a5', padding: '0.75rem 1rem', borderRadius: 'var(--radius-md)', display: 'flex', alignItems: 'center', gap: '0.6rem' }}>
          <AlertCircle size={18} /> {error}
        </div>
      )}

      {success && (
        <div style={{ background: 'rgba(16, 185, 129, 0.15)', border: '1px solid var(--accent-emerald)', color: '#6ee7b7', padding: '0.75rem 1rem', borderRadius: 'var(--radius-md)', display: 'flex', alignItems: 'center', gap: '0.6rem' }}>
          <CheckCircle size={18} /> {success}
        </div>
      )}

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '1.25rem' }}>
        {accounts.map((acc) => (
          <div key={acc.accountId} className="glass-panel glass-card-interactive" style={{ padding: '1.5rem' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
              <span style={{ fontSize: '0.85rem', color: '#818cf8', fontWeight: 700, textTransform: 'uppercase' }}>
                {acc.accountType} Account
              </span>
              <span style={{ background: acc.status === 'Active' ? 'rgba(16, 185, 129, 0.2)' : 'rgba(244, 63, 94, 0.2)', color: acc.status === 'Active' ? '#34d399' : '#f87171', padding: '0.25rem 0.75rem', borderRadius: '20px', fontSize: '0.8rem', fontWeight: 600 }}>
                {acc.status}
              </span>
            </div>

            <div style={{ marginBottom: '1.25rem' }}>
              <span style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Account Balance</span>
              <h2 style={{ fontSize: '2rem', fontWeight: 700, color: '#fff' }}>
                ₹{parseFloat(acc.balance).toLocaleString('en-IN', { minimumFractionDigits: 2 })}
              </h2>
            </div>

            <div style={{ borderTop: '1px solid var(--border-glass)', paddingTop: '1rem', display: 'flex', flexDirection: 'column', gap: '0.4rem', fontSize: '0.85rem', color: 'var(--text-muted)' }}>
              <div><strong>Account ID:</strong> {formatAccountId(acc.accountId)}</div>
              <div><strong>Account Number:</strong> {formatAccountNumber(acc.accountNumber)}</div>
              <div><strong>Branch:</strong> {acc.branchName}</div>
              <div><strong>IFSC Code:</strong> {acc.ifscCode}</div>
              <div><strong>Opened:</strong> {acc.openDate}</div>
            </div>

            {acc.status === 'Active' && (
              <div style={{ marginTop: '1.25rem', borderTop: '1px solid var(--border-glass)', paddingTop: '1rem', textAlign: 'right' }}>
                <button className="btn-danger" style={{ padding: '0.4rem 0.85rem', fontSize: '0.8rem' }} onClick={() => handleCloseAccount(acc.accountId)}>
                  <XCircle size={14} /> Close Account
                </button>
              </div>
            )}
          </div>
        ))}
      </div>

      {/* Create Account Modal */}
      {modalOpen && (
        <div style={{ position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.8)', backdropFilter: 'blur(8px)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 100 }}>
          <div className="glass-panel animate-fade-in" style={{ width: '100%', maxWidth: '420px', padding: '2rem' }}>
            <h3 style={{ fontSize: '1.4rem', color: '#fff', marginBottom: '1.25rem' }}>Open New Bank Account</h3>
            <form onSubmit={handleCreateAccount} noValidate>
              <div className="form-group" style={{ marginBottom: '1.25rem' }}>
                <label className="form-label">Account Type</label>
                <select className="form-select" value={newAccData.accountType} onChange={(e) => setNewAccData({ ...newAccData, accountType: e.target.value })}>
                  <option value="Savings">Savings Account</option>
                  <option value="Current">Current Account</option>
                  <option value="Salary">Salary Account</option>
                </select>
              </div>

              <div className="form-group" style={{ marginBottom: '1.25rem' }}>
                <label className="form-label">Branch Name</label>
                <input
                  type="text"
                  className={`form-input ${modalErrors.branchName ? 'form-input-error' : ''}`}
                  placeholder="e.g. Main City Branch"
                  value={newAccData.branchName}
                  onChange={(e) => {
                    setNewAccData({ ...newAccData, branchName: e.target.value });
                    if (modalErrors.branchName) setModalErrors({ ...modalErrors, branchName: null });
                  }}
                />
                {modalErrors.branchName && <div className="error-text"><AlertCircle size={13} /> {modalErrors.branchName}</div>}
              </div>

              <div className="form-group" style={{ marginBottom: '1.5rem' }}>
                <label className="form-label">IFSC Code</label>
                <input
                  type="text"
                  className={`form-input ${modalErrors.ifscCode ? 'form-input-error' : ''}`}
                  placeholder="e.g. BKID000101"
                  value={newAccData.ifscCode}
                  onChange={(e) => {
                    setNewAccData({ ...newAccData, ifscCode: e.target.value.toUpperCase() });
                    if (modalErrors.ifscCode) setModalErrors({ ...modalErrors, ifscCode: null });
                  }}
                  maxLength={11}
                />
                {modalErrors.ifscCode && <div className="error-text"><AlertCircle size={13} /> {modalErrors.ifscCode}</div>}
              </div>

              <div style={{ display: 'flex', gap: '0.75rem', marginTop: '1.5rem' }}>
                <button className="btn-secondary" type="button" style={{ flex: 1 }} onClick={() => setModalOpen(false)}>Cancel</button>
                <button className="btn-primary" type="submit" style={{ flex: 1 }}>Create</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
