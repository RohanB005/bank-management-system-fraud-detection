import authAxios from "../api/authAxios";

export const login = (data) => {

    return authAxios.post("/login", data);

};

export const register = (data) => {

    return authAxios.post("/register", data);

};

export const verifyOTP = (data) => {

    return authAxios.post("/verify-otp", data);

};

export const forgotPassword = (data) => {

    return authAxios.post("/forgot-password", data);

};

export const resetPassword = (data) => {

    return authAxios.post("/reset-password", data);

};