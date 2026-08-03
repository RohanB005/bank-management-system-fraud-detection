import { useEffect, useState } from "react";
import { toast, ToastContainer } from "react-toastify";

import MainLayout from "../../components/layout/MainLayout";

import { getAccountsByCustomer } from "../../services/accountService";

import { deposit } from "../../services/transactionService";
import { getTransactionCity } from "../../services/locationService";

function Deposit() {

    const customerId = localStorage.getItem("customerId");

    const [accounts, setAccounts] = useState([]);

    const [loading, setLoading] = useState(false);

    const [city, setCity] = useState("");

    const [formData, setFormData] = useState({

        accountId: "",

        amount: "",

        description: ""

    });

    useEffect(() => {

        loadAccounts();

        loadCity();

    }, []);

    const loadCity = async () => {

    const detectedCity = await getTransactionCity();

    setCity(detectedCity);

};

    const loadAccounts = async () => {

        try {

            const response = await getAccountsByCustomer(customerId);

            setAccounts(response.data.data);

        }

        catch (error) {

            console.log(error);

        }

    };


    const handleChange = (e) => {

        setFormData({

            ...formData,

            [e.target.name]: e.target.value

        });

    };

const handleSubmit = async (e) => {

    e.preventDefault();

    try {

        setLoading(true);

        const request = {

            accountId: Number(formData.accountId),

            amount: Number(formData.amount),

            description: formData.description,

            transactionCity: city

        };

        console.log(request);

        const response = await deposit(request);

        toast.success(response.data.message);

        setFormData({

            accountId: "",

            amount: "",

            description: ""

        });

    }

    catch (error) {

        console.log(error);

        toast.error(

            error.response?.data?.message ||

            "Deposit Failed"

        );

    }

    finally {

        setLoading(false);

    }

};

    return (

        <MainLayout>

            <ToastContainer />

            <div className="row justify-content-center">

                <div className="col-md-7">

                    <div className="card shadow">

                        <div className="card-header bg-success text-white">

                            <h3>

                                Deposit Money

                            </h3>

                        </div>

                        <div className="card-body">

                                <form onSubmit={handleSubmit}>

    <div className="mb-3">

        <label>

            Account

        </label>

        <select
            className="form-select"
            name="accountId"
            value={formData.accountId}
            onChange={handleChange}
            required
        >

            <option value="">

                Select Account

            </option>

            {

                accounts
                    .filter(a => a.status === "Active")
                    .map(account => (

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

    <div className="mb-3">

        <label>

            Amount

        </label>

        <input
            type="number"
            className="form-control"
            name="amount"
            value={formData.amount}
            onChange={handleChange}
            required
        />

    </div>

    <div className="mb-3">

        <label>

            Description

        </label>

        <textarea
            className="form-control"
            rows="3"
            name="description"
            value={formData.description}
            onChange={handleChange}
        />

    </div>


    <button
        className="btn btn-success w-100"
        disabled={loading}
    >

        {

            loading

                ?

                "Depositing..."

                :

                "Deposit"

        }

    </button>

</form>

                        </div>

                    </div>

                </div>

            </div>

        </MainLayout>

    );

}

export default Deposit;