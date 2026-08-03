import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";
import { register } from "../../services/authService";

function Register() {

    const navigate = useNavigate();

    const [loading, setLoading] = useState(false);

    const [formData, setFormData] = useState({

        firstName: "",
        lastName: "",
        dateOfBirth: "",
        gender: "",
        email: "",
        mobile: "",
        password: "",
        confirmPassword: "",
        aadhaar: "",
        pan: "",
        address: "",
        city: "",
        state: "",
        pincode: ""

    });

    const handleChange = (e) => {

        setFormData({

            ...formData,

            [e.target.name]: e.target.value

        });

    };

const handleSubmit = async (e) => {

    e.preventDefault();

    if (formData.password !== formData.confirmPassword) {

        toast.error("Passwords do not match");

        return;

    }

    const requestData = {

        firstName: formData.firstName,

        lastName: formData.lastName,

        dateOfBirth: formData.dateOfBirth,

        gender: formData.gender,

        email: formData.email,

        mobile: formData.mobile,

        password: formData.password,

        aadhaar: formData.aadhaar,

        pan: formData.pan,

        address: formData.address,

        city: formData.city,

        state: formData.state,

        pincode: formData.pincode

    };

    try {

        setLoading(true);

        const response = await register(requestData);

        toast.success(response.data.message);

        setTimeout(() => {

            navigate("/");

        }, 1500);

    }

    catch (error) {

        toast.error(

            error.response?.data?.message ||

            "Registration Failed"

        );

    }

    finally {

        setLoading(false);

    }

};

    return (

        <>
            <ToastContainer />

            <div className="container mt-5 mb-5">

                <div className="card shadow">

                    <div className="card-header bg-primary text-white">

                        <h3 className="mb-0">

                            Customer Registration

                        </h3>

                    </div>

                    <div className="card-body">

                        <form onSubmit={handleSubmit}>

                            <div className="row">

    <div className="col-md-6 mb-3">

        <label>First Name</label>

        <input
            type="text"
            className="form-control"
            name="firstName"
            value={formData.firstName}
            onChange={handleChange}
        />

    </div>

                            <div className="col-md-6 mb-3">

                                <label>Last Name</label>

                                <input
                                    type="text"
                                    className="form-control"
                                    name="lastName"
                                    value={formData.lastName}
                                    onChange={handleChange}
                                />

                            </div>

                        </div>
                        <div className="row">

    <div className="col-md-6 mb-3">

        <label>Date of Birth</label>

        <input
            type="date"
            className="form-control"
            name="dateOfBirth"
            value={formData.dateOfBirth}
            onChange={handleChange}
        />

    </div>

    <div className="col-md-6 mb-3">

        <label>Gender</label>

        <select
            className="form-select"
            name="gender"
            value={formData.gender}
            onChange={handleChange}
        >

            <option value="">Select</option>

            <option value="MALE">Male</option>

            <option value="FEMALE">Female</option>

            <option value="OTHER">Other</option>

        </select>

    </div>

</div>

<hr />

<h5 className="mb-3">

    Contact Information

</h5>

<div className="row">

    <div className="col-md-6 mb-3">

        <label>Email</label>

        <input
            type="email"
            className="form-control"
            name="email"
            value={formData.email}
            onChange={handleChange}
        />

    </div>

    <div className="col-md-6 mb-3">

        <label>Mobile Number</label>

        <input
            type="text"
            className="form-control"
            name="mobile"
            value={formData.mobile}
            onChange={handleChange}
            maxLength="10"
        />

    </div>

</div>

<hr />

<h5 className="mb-3">

    Identity Information

</h5>

<div className="row">

    <div className="col-md-6 mb-3">

        <label>Aadhaar Number</label>

        <input
            type="text"
            className="form-control"
            name="aadhaar"
            value={formData.aadhaar}
            onChange={handleChange}
            maxLength="12"
        />

    </div>

    <div className="col-md-6 mb-3">

        <label>PAN Number</label>

        <input
            type="text"
            className="form-control"
            name="pan"
            value={formData.pan}
            onChange={handleChange}
            style={{ textTransform: "uppercase" }}
        />

    </div>

</div>

<hr />

<h5 className="mb-3">

    Address Information

</h5>

<div className="mb-3">

    <label>Address</label>

    <textarea
        className="form-control"
        rows="3"
        name="address"
        value={formData.address}
        onChange={handleChange}
    />

</div>

<div className="row">

    <div className="col-md-4 mb-3">

        <label>City</label>

        <input
            type="text"
            className="form-control"
            name="city"
            value={formData.city}
            onChange={handleChange}
        />

    </div>

    <div className="col-md-4 mb-3">

        <label>State</label>

        <input
            type="text"
            className="form-control"
            name="state"
            value={formData.state}
            onChange={handleChange}
        />

    </div>

    <div className="col-md-4 mb-3">

        <label>Pincode</label>

        <input
            type="text"
            className="form-control"
            name="pincode"
            value={formData.pincode}
            onChange={handleChange}
            maxLength="6"
        />

    </div>

</div>

<hr />

<h5 className="mb-3">

    Security

</h5>

<div className="row">

    <div className="col-md-6 mb-3">

        <label>Password</label>

        <input
            type="password"
            className="form-control"
            name="password"
            value={formData.password}
            onChange={handleChange}
        />

    </div>

    <div className="col-md-6 mb-3">

        <label>Confirm Password</label>

        <input
            type="password"
            className="form-control"
            name="confirmPassword"
            value={formData.confirmPassword}
            onChange={handleChange}
        />

    </div>

</div>

<div className="text-center mt-4">

    <button
        className="btn btn-primary px-5"
        type="submit"
        disabled={loading}
    >

        {

            loading

                ?

                "Registering..."

                :

                "Register"

        }

    </button>

</div>


                        </form>

                    </div>

                    <div className="card-footer text-center">

                        Already have an account?

                        <Link to="/" className="ms-2">

                            Login

                        </Link>

                    </div>

                </div>

            </div>

        </>

    );

}

export default Register;