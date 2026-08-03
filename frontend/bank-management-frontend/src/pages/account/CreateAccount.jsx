import { useState } from "react";

import { useNavigate } from "react-router-dom";

import { toast, ToastContainer } from "react-toastify";

import MainLayout from "../../components/layout/MainLayout";

import { createAccount } from "../../services/accountService";

function CreateAccount() {

    const navigate = useNavigate();

    const customerId = localStorage.getItem("customerId");

    const [loading, setLoading] = useState(false);

const [formData, setFormData] = useState({

    accountType: "",

    branchName: "",

    ifscCode: ""

});

    const handleChange = (e) => {

        setFormData({

            ...formData,

            [e.target.name]: e.target.value

        });

    };

const handleSubmit = async (e) => {

    e.preventDefault();

    const requestData = {

        customerId: Number(customerId),

        accountType: formData.accountType,

        branchName: formData.branchName,

        ifscCode: formData.ifscCode

    };

    try {

        setLoading(true);

        const response = await createAccount(requestData);

        toast.success(response.data.message);

        setTimeout(() => {

            navigate("/accounts");

        }, 1500);

    }

    catch (error) {

        console.log(error);

        toast.error(

            error.response?.data?.message ||

            "Unable to create account."

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

                        <div className="card-header bg-primary text-white">

                            <h3>

                                Create New Account

                            </h3>

                        </div>

                        <div className="card-body">

                            <form onSubmit={handleSubmit}>

    {/* Account Type */}

    <div className="mb-3">

        <label className="form-label">

            Account Type

            <span className="text-danger">*</span>

        </label>

        <select
            className="form-select"
            name="accountType"
            value={formData.accountType}
            onChange={handleChange}
            required
        >

            <option value="">

                Select Account Type

            </option>

            <option value="Savings">

                Savings

            </option>

            <option value="Current">

                Current

            </option>

            <option value="Salary">

                Salary

            </option>

        </select>

    </div>

    {/* Branch Name */}

    <div className="mb-3">

        <label className="form-label">

            Branch Name

            <span className="text-danger">*</span>

        </label>

        <input
            type="text"
            className="form-control"
            name="branchName"
            value={formData.branchName}
            onChange={handleChange}
            placeholder="Enter Branch Name"
            required
        />

    </div>

    {/* IFSC */}

    <div className="mb-4">

        <label className="form-label">

            IFSC Code

            <span className="text-danger">*</span>

        </label>

        <input
            type="text"
            className="form-control"
            name="ifscCode"
            value={formData.ifscCode}
            onChange={handleChange}
            placeholder="Enter IFSC Code"
            required
        />

    </div>

    <div className="d-grid">

        <button
            className="btn btn-primary"
            disabled={loading}
        >

            {

                loading

                    ?

                    "Creating Account..."

                    :

                    "Create Account"

            }

        </button>

    </div>

</form>

                        </div>

                    </div>

                </div>

            </div>

        </MainLayout>

    );

}

export default CreateAccount;