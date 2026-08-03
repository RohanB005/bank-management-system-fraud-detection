import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";

import MainLayout from "../../components/layout/MainLayout";
import { getAccountsByCustomer } from "../../services/accountService";
//import { getAccountsByCustomer, closeAccount } from "../../services/accountService";
function AccountList() {

    const [accounts, setAccounts] = useState([]);

    const customerId = localStorage.getItem("customerId");

    useEffect(() => {

        loadAccounts();

    }, []);

    const loadAccounts = async () => {

        try {

            const response = await getAccountsByCustomer(customerId);

            console.log(response.data);

            setAccounts(response.data.data);

        }

        catch (error) {

            console.log(error);

            toast.error("Unable to load accounts.");

        }

    };

    const handleClose = async (accountId) => {

    const confirmClose = window.confirm(
        "Are you sure you want to close this account?"
    );

    if (!confirmClose) {
        return;
    }

    try {

        const response = await closeAccount(accountId);

        toast.success(response.data.message);

        loadAccounts();

    }

    catch (error) {

        console.log(error);

        toast.error(
            error.response?.data?.message ||
            "Unable to close account."
        );

    }

};

    return (

        <MainLayout>

            <ToastContainer />

            <h2 className="mb-4">

                My Accounts

            </h2>

            <div className="card shadow">

                <div className="card-body">

                    <table className="table table-hover">

                        <thead className="table-dark">

                            <tr>

                                <th>Account No</th>

                                <th>Type</th>

                                <th>Balance</th>

                                <th>Status</th>

                                <th>Actions</th>

                            </tr>

                        </thead>

                        <tbody>

                            {

                                accounts.length === 0 ?

                                    (

                                        <tr>

                                            <td colSpan="5" className="text-center">

                                                No Accounts Found

                                            </td>

                                        </tr>

                                    )

                                    :

                                    accounts.map((account) => (

                                        <tr key={account.accountId}>

                                            <td>

                                                {account.accountNumber}

                                            </td>

                                            <td>

                                                {account.accountType}

                                            </td>

                                            <td>

                                                ₹ {account.balance}

                                            </td>

                                            <td>

                                                {account.status}

                                            </td>

                                            <td>

                                                <div className="d-flex gap-2">

    <Link
        className="btn btn-sm btn-primary"
        to={`/accounts/${account.accountId}`}
    >
        View
    </Link>

    {
        account.status === "Active" && (
            <button
                className="btn btn-sm btn-danger"
                onClick={() => handleClose(account.accountId)}
            >
                Close
            </button>
        )
    }

</div>
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

export default AccountList;