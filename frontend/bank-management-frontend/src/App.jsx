import { Routes, Route } from "react-router-dom";
import ProtectedRoute from "./components/layout/ProtectedRoute";

// Auth Pages
import Login from "./pages/auth/Login";
import Register from "./pages/auth/Register";
import VerifyOTP from "./pages/auth/VerifyOTP";
import ForgotPassword from "./pages/auth/ForgotPassword";
import ResetPassword from "./pages/auth/ResetPassword";

// Dashboard
import Dashboard from "./pages/dashboard/Dashboard";

// Account
import CreateAccount from "./pages/account/CreateAccount";
import AccountList from "./pages/account/AccountList";
import AccountDetails from "./pages/account/AccountDetails";

// Transfer
import Transfer from "./pages/transfer/Transfer";

// Transaction
import Deposit from "./pages/transaction/Deposit";
import Withdraw from "./pages/transaction/Withdraw";
import History from "./pages/transaction/History";
import TransactionDetails from "./pages/transaction/TransactionDetails";

// Profile
import Profile from "./pages/profile/Profile";

// Chatbot
import Chatbot from "./pages/chatbot/Chatbot";

function App() {
    return (
        <Routes>

            {/* Authentication */}
            <Route path="/" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/verify-otp" element={<VerifyOTP />} />
            <Route path="/forgot-password" element={<ForgotPassword />} />
            <Route path="/reset-password" element={<ResetPassword />} />

            {/* Dashboard */}
            <Route path="/dashboard" element={
              <ProtectedRoute>
                <Dashboard />
              </ProtectedRoute>
            }
             />

            {/* Account */}
            <Route path="/accounts/create" element={
              <ProtectedRoute>
                <CreateAccount />
              </ProtectedRoute>
              } 
              />
            <Route path="/accounts" element={
              <ProtectedRoute>
                <AccountList />
              </ProtectedRoute>
              } 
              />
            <Route path="/accounts/:id" element={
              <ProtectedRoute>
                <AccountDetails />
              </ProtectedRoute>              
              }
               />

            {/* Transaction */}
            <Route path="/deposit" element={
              <ProtectedRoute>
                <Deposit />
              </ProtectedRoute>
              }
               />
            <Route path="/withdraw" element={
              <ProtectedRoute>
                <Withdraw />
              </ProtectedRoute>
              } 
              />
            <Route path="/history" element={
              <ProtectedRoute>
                <History />
              </ProtectedRoute>
              } 
              />
            <Route path="/transaction/:id" element={
               <ProtectedRoute>
                <TransactionDetails />
               </ProtectedRoute>
              } />

            {/* Transfer */}
            <Route path="/transfer" element={
              <ProtectedRoute>
                 <Transfer />
              </ProtectedRoute>
             } />

            {/* Profile */}
            <Route path="/profile" element={
              <ProtectedRoute>
                <Profile />
              </ProtectedRoute>
              } />

            {/* Chatbot */}
            <Route path="/chatbot" element={
               <ProtectedRoute>
                <Chatbot />
               </ProtectedRoute>
              } />

        </Routes>
    );
}

export default App;