import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import { getAccountsByCustomer } from "../../services/accountService";
import { transferMoney } from "../../services/transactionService";
import { getTransactionCity } from "../../services/locationService";

function Transfer() {

    const [accounts, setAccounts] = useState([]);

    const [formData, setFormData] = useState({
        fromAccountId: "",
        toAccountId: "",
        amount: "",
        transactionCity: ""
    });

    const [loading, setLoading] = useState(false);

    useEffect(() => {
        loadAccounts();
        loadCity();
    }, []);

    const loadAccounts = async () => {
        try {

            const customerId = localStorage.getItem("customerId");

           const response = await getAccountsByCustomer(customerId);

setAccounts(
    response.data.data.filter(
        account => account.status === "Active"
    )
);

        } catch (error) {

            toast.error("Unable to load accounts");

        }
    };

    const loadCity = async () => {

        const city = await getTransactionCity();

        setFormData(prev => ({
            ...prev,
            transactionCity: city
        }));

    };

    const handleChange = (e) => {

        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });

    };

    const handleSubmit = async (e) => {

        e.preventDefault();

        if (
            !formData.fromAccountId ||
            !formData.toAccountId ||
            !formData.amount
        ) {
            toast.error("Please fill all fields");
            return;
        }

        try {

            setLoading(true);

            const city = await getTransactionCity();

const request = {
    ...formData,
    transactionCity: city
};

const response = await transferMoney(request);

            toast.success(response.data.message);

        } catch (error) {

            toast.error(
                error.response?.data?.message ||
                "Transfer Failed"
            );

        } finally {

            setLoading(false);

        }

    };

    return (

        <div className="container mt-4">

            <div className="card shadow">

                <div className="card-header">

                    <h3>Transfer Money</h3>

                </div>

                <div className="card-body">

                    <form onSubmit={handleSubmit}>

                        <div className="mb-3">

                            <label>From Account</label>

                            <select
                                className="form-select"
                                name="fromAccountId"
                                value={formData.fromAccountId}
                                onChange={handleChange}
                            >

                                <option value="">Select Account</option>

                                {accounts.map(account => (

                                    <option
                                        key={account.accountId}
                                        value={account.accountId}
                                    >

                                        {account.accountType} - {account.accountNumber} (₹{account.balance})

                                    </option>

                                ))}

                            </select>

                        </div>

                        <div className="mb-3">

                            <label>To Account ID</label>

                            <input
                                type="number"
                                className="form-control"
                                name="toAccountId"
                                value={formData.toAccountId}
                                onChange={handleChange}
                            />

                        </div>

                        <div className="mb-3">

                            <label>Amount</label>

                            <input
                                type="number"
                                className="form-control"
                                name="amount"
                                value={formData.amount}
                                onChange={handleChange}
                            />

                        </div>

                        <button
                            className="btn btn-primary"
                            disabled={loading}
                        >

                            {loading
                                ? "Processing..."
                                : "Transfer Money"}

                        </button>

                    </form>

                </div>

            </div>

        </div>

    );

}

export default Transfer;