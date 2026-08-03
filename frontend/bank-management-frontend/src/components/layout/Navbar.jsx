import { Link, useNavigate } from "react-router-dom";

function Navbar() {

    const navigate = useNavigate();

    const firstName = localStorage.getItem("firstName");

    const logout = () => {

        localStorage.clear();

        navigate("/");

    };

    return (

        <nav className="navbar navbar-expand-lg navbar-dark bg-primary">

            <div className="container">

                <Link className="navbar-brand" to="/dashboard">

                    🏦 Secure Bank

                </Link>

                <div className="ms-auto d-flex align-items-center">

                    <span className="text-white me-3">

                        Welcome, {firstName}

                    </span>

                    <button
                        className="btn btn-light btn-sm"
                        onClick={logout}
                    >

                        Logout

                    </button>

                </div>

            </div>

        </nav>

    );

}

export default Navbar;