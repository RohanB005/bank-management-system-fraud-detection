import { NavLink } from "react-router-dom";

function Sidebar() {

    return (

        <div
            className="bg-dark text-white vh-100 p-3"
            style={{
                width: "260px",
                position: "fixed",
                left: 0,
                top: 0
            }}
        >

            <h3 className="text-center mb-4">

                🏦 Secure Bank

            </h3>

            <NavLink
                to="/dashboard"
                className="btn btn-dark w-100 text-start mb-2"
            >
                🏠 Dashboard
            </NavLink>

            <NavLink
                to="/accounts/create"
                className="btn btn-dark w-100 text-start mb-2"
            >
                💳 Create Account
            </NavLink>

            <NavLink
                to="/accounts"
                className="btn btn-dark w-100 text-start mb-2"
            >
                📋 My Accounts
            </NavLink>

            <NavLink
                to="/deposit"
                className="btn btn-dark w-100 text-start mb-2"
            >
                💰 Deposit
            </NavLink>

            <NavLink
                to="/withdraw"
                className="btn btn-dark w-100 text-start mb-2"
            >
                🏧 Withdraw
            </NavLink>

            <NavLink
                to="/transfer"
                className="btn btn-dark w-100 text-start mb-2"
            >
                🔄 Transfer
            </NavLink>

            <NavLink
                to="/history"
                className="btn btn-dark w-100 text-start mb-2"
            >
                📜 Transaction History
            </NavLink>

            <NavLink
                to="/profile"
                className="btn btn-dark w-100 text-start mb-2"
            >
                👤 Profile
            </NavLink>

            <NavLink
                to="/chatbot"
                className="btn btn-dark w-100 text-start"
            >
                🤖 AI Chatbot
            </NavLink>

        </div>

    );

}

export default Sidebar;