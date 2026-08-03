import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";

import MainLayout from "../../components/layout/MainLayout";
import { getAccountById } from "../../services/accountService";

function AccountDetails() {

    const { id } = useParams();

    const [account, setAccount] = useState(null);

    useEffect(() => {

        loadAccount();

    }, []);

    const loadAccount = async () => {

        try {

            const response = await getAccountById(id);

            console.log(response.data);

            setAccount(response.data.data);

        }

        catch (error) {

            console.log(error);

            toast.error("Unable to load account details.");

        }

    };

    if (!account) {

        return (

            <MainLayout>

                <div className="text-center mt-5">

                    Loading...

                </div>

            </MainLayout>

        );

    }

    return (

        <MainLayout>

            <ToastContainer />

            <h2 className="mb-4">

                Account Details

            </h2>

            <div className="card shadow">

                <div className="card-body">

                    <div className="row">

                        <div className="col-md-6 mb-3">

                            <strong>Account Number</strong>

                            <p>{account.accountNumber}</p>

                        </div>

                        <div className="col-md-6 mb-3">

                            <strong>Account Type</strong>

                            <p>{account.accountType}</p>

                        </div>

                        <div className="col-md-6 mb-3">

                            <strong>Balance</strong>

                            <p>₹ {account.balance}</p>

                        </div>

                        <div className="col-md-6 mb-3">

                            <strong>Status</strong>

                            <p>{account.status}</p>

                        </div>

                        <div className="col-md-6 mb-3">

                            <strong>Branch Name</strong>

                            <p>{account.branchName}</p>

                        </div>

                        <div className="col-md-6 mb-3">

                            <strong>IFSC Code</strong>

                            <p>{account.ifscCode}</p>

                        </div>

                    </div>

                </div>

            </div>

        </MainLayout>

    );

}

export default AccountDetails;