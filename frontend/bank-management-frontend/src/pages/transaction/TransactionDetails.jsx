import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";

import MainLayout from "../../components/layout/MainLayout";
import { getTransactionById } from "../../services/transactionService";

function TransactionDetails() {

    const { id } = useParams();

    const navigate = useNavigate();

    const [transaction, setTransaction] = useState(null);

    useEffect(() => {

        loadTransaction();

    }, []);

    const loadTransaction = async () => {

        try {

            const response = await getTransactionById(id);

            console.log(response.data);

            setTransaction(response.data.data);

        }

        catch (error) {

            console.log(error);

            toast.error("Unable to load transaction details.");

        }

    };

    if (!transaction) {

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

            <div className="d-flex justify-content-between align-items-center mb-4">

                <h2>

                    Transaction Details

                </h2>

                <button
                    className="btn btn-secondary"
                    onClick={() => navigate(-1)}
                >

                    Back

                </button>

            </div>

            <div className="card shadow">

                <div className="card-body">

                    <div className="row">

                        <div className="col-md-6 mb-3">

                            <strong>

                                Reference Number

                            </strong>

                            <p>

                                {transaction.referenceNumber}

                            </p>

                        </div>

                        <div className="col-md-6 mb-3">

                            <strong>

                                Transaction Type

                            </strong>

                            <p>

                                {transaction.transactionType}

                            </p>

                        </div>

                        <div className="col-md-6 mb-3">

                            <strong>

                                Amount

                            </strong>

                            <p>

                                ₹ {transaction.amount}

                            </p>

                        </div>

                        <div className="col-md-6 mb-3">

                            <strong>

                                Available Balance

                            </strong>

                            <p>

                                ₹ {transaction.availableBalance}

                            </p>

                        </div>

                        <div className="col-md-6 mb-3">

                            <strong>

                                Status

                            </strong>

                            <p>

                                {transaction.status}

                            </p>

                        </div>

                        <div className="col-md-6 mb-3">

                            <strong>

                                Transaction Time

                            </strong>

                            <p>

                                {

                                    new Date(transaction.transactionTime)

                                        .toLocaleString()

                                }

                            </p>

                        </div>

                        

                    </div>

                </div>

            </div>

        </MainLayout>

    );

}

export default TransactionDetails;