import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";

import { login } from "../../services/authService";

function Login() {

    const navigate = useNavigate();

    const [showPassword, setShowPassword] = useState(false);

    const [loading, setLoading] = useState(false);

    const [formData, setFormData] = useState({

        email: "",

        password: ""

    });

    const handleChange = (e) => {

        setFormData({

            ...formData,

            [e.target.name]: e.target.value

        });

    };

const handleSubmit = async (e) => {

    e.preventDefault();

    if (!formData.email || !formData.password) {

        toast.error("Please fill all fields");

        return;

    }

    try {

        setLoading(true);

        const response = await login(formData);

        console.log(response.data);

        const data = response.data;

        localStorage.setItem("token", data.token);

        localStorage.setItem("customerId", data.customerId);

        localStorage.setItem("firstName", data.firstName);

        localStorage.setItem("lastName", data.lastName);

        localStorage.setItem("email", data.email);

        toast.success(data.message);

        setTimeout(() => {

            navigate("/dashboard");

        }, 1200);

    }

    catch (error) {

        console.log(error);

        toast.error(

            error.response?.data?.message ||

            "Invalid Email or Password"

        );

    }

    finally {

        setLoading(false);

    }

};

    return (

        <>
            <ToastContainer />

          <div
                    className="container-fluid vh-100 d-flex align-items-center"
                    style={{
                        
                        background: "linear-gradient(135deg,#0d6efd,#0b5ed7)"
                    }}
                >

                <div className="row justify-content-center align-items-center vh-100">

                    <div className="col-lg-6 d-flex align-items-center justify-content-center bg-primary text-white">

                        <div className="text-center">

                            <h1 className="display-4 fw-bold">

                                Secure Bank

                            </h1>

                            <h4 className="mt-3">

                                Bank Management System

                            </h4>

                            <p className="mt-4">

                                Safe • Secure • Reliable

                            </p>

                        </div>

                    </div>

                    <div className="col-lg-6 d-flex align-items-center justify-content-center">

                        <div
                                className="card shadow-lg p-5"
                                style={{
                                    width: "470px",
                                    borderRadius: "18px"
                                }}
                            >

                             <div className="text-center mb-4">

    <h2 className="fw-bold">

        Welcome Back

    </h2>

    <p className="text-muted">

        Sign in to your account

    </p>

</div>   

                            <form onSubmit={handleSubmit}>

                                <div className="mb-3">

                                   <label className="fw-semibold">

Email

<span className="text-danger">*</span>

</label>

                                    <input
                                        type="email"
                                        className="form-control"
                                        name="email"
                                        value={formData.email}
                                        onChange={handleChange}
                                        placeholder="Enter Email"
                                    />

                                </div>

                                <div className="mb-3">

                                   <label className="fw-semibold">

Password

<span className="text-danger">*</span>

</label>

                                    <div className="input-group">

                                        <input
                                            type={showPassword ? "text" : "password"}
                                            className="form-control form-control-lg"
                                            name="password"
                                            value={formData.password}
                                            onChange={handleChange}
                                            placeholder="Enter Password"
                                        />

                                        <button
                                            type="button"
                                            className="btn btn-outline-primary"
                                            onClick={() => setShowPassword(!showPassword)}
                                        >
                                            {showPassword ? "Hide" : "Show"}
                                        </button>

                                    </div>

                                </div>

                                <button
                                    className="btn btn-primary btn-lg w-100"
                                    disabled={loading}
                                >

                                    {

                                        loading

                                            ? "Logging in..."

                                            : "Login"

                                    }

                                </button>

                            </form>

                            <div className="text-center mt-3">

                                <Link to="/forgot-password">

                                    Forgot Password?

                                </Link>

                            </div>

                            <div className="text-center mt-3">

                                Don't have an account?

                                <Link to="/register">

                                    Register

                                </Link>

                            </div>

                                <hr />

                    <p className="small text-muted text-center mt-4">

© 2026 Secure Bank

</p>
                            

                        </div>

                       

                    </div>

                  

                </div>

                

            </div>

        </>

    );

}

export default Login;