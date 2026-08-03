import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";

import MainLayout from "../../components/layout/MainLayout";

import { getAccountsByCustomer } from "../../services/accountService";
import { getTransactionHistory } from "../../services/transactionService";

function History() {

    const customerId = localStorage.getItem("customerId");

    const [accounts, setAccounts] = useState([]);

    const [selectedAccount, setSelectedAccount] = useState("");

    const [transactions, setTransactions] = useState([]);

    useEffect(() => {

        loadAccounts();

    }, []);

    const loadAccounts = async () => {

        try {

            const response = await getAccountsByCustomer(customerId);

            setAccounts(response.data.data);

        }

        catch (error) {

            console.log(error);

        }

    };

    const loadTransactions = async (accountId) => {

        try {

            const response = await getTransactionHistory(accountId);

            setTransactions(response.data.data);

        }

        catch (error) {

            console.log(error);

            toast.error("Unable to load transactions.");

        }

    };

    const handleAccountChange = (e) => {

        const accountId = e.target.value;

        setSelectedAccount(accountId);

        if (accountId !== "") {

            loadTransactions(accountId);

        }

    };

    return (

        <MainLayout>

            <ToastContainer />

            <h2 className="mb-4">

                Transaction History

            </h2>

            <div className="card shadow">

                <div className="card-body">

                    <div className="mb-4">

                        <label className="form-label">

                            Select Account

                        </label>

                        <select
                            className="form-select"
                            value={selectedAccount}
                            onChange={handleAccountChange}
                        >

                            <option value="">

                                Select Account

                            </option>

                            {

                                accounts.map(account => (

                                    <option
                                        key={account.accountId}
                                        value={account.accountId}
                                    >

                                        {account.accountNumber}

                                        {" - "}

                                        {account.accountType}

                                    </option>

                                ))

                            }

                        </select>

                    </div>

                    <table className="table table-bordered table-hover">

                        <thead className="table-dark">

                            <tr>

                                <th>Reference</th>

                                <th>Type</th>

                                <th>Amount</th>

                                <th>Balance</th>

                                <th>Status</th>

                                <th>Date</th>

                                <th>Action</th>

                            </tr>

                        </thead>

                        <tbody>

                            {

                                transactions.length === 0 ?

                                    (

                                        <tr>

                                            <td colSpan="7" className="text-center">

                                                No Transactions Found

                                            </td>

                                        </tr>

                                    )

                                    :

                                    transactions.map(transaction => (

                                        <tr key={transaction.transactionId}>

                                            <td>

                                                {transaction.referenceNumber}

                                            </td>

                                            <td>

                                                {transaction.transactionType}

                                            </td>

                                            <td>

                                                ₹ {transaction.amount}

                                            </td>

                                            <td>

                                                ₹ {transaction.availableBalance}

                                            </td>

                                            <td>

                                                {transaction.status}

                                            </td>

                                            <td>

                                                {

                                                    new Date(transaction.transactionTime)

                                                        .toLocaleString()

                                                }

                                            </td>

                                            <td>

                                                <Link
                                                    className="btn btn-primary btn-sm"
                                                    to={`/transaction/${transaction.transactionId}`}
                                                >

                                                    View

                                                </Link>

                                            </td>

                                        </tr>

                                    ))

                            }

                        </tbody>

                    </table>

                </div>

            </div>

        </MainLayout>

    );

}

export default History;