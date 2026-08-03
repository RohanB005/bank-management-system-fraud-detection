export const saveLoginData = (response) => {

    localStorage.setItem("token", response.token);

    localStorage.setItem("customerId", response.customerId);

    localStorage.setItem("email", response.email);

};

export const logout = () => {

    localStorage.clear();

};

export const isLoggedIn = () => {

    return localStorage.getItem("token") !== null;

};