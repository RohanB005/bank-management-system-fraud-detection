import accountAxios from "../api/accountAxios";

export const createAccount = (data) => {

    return accountAxios.post("", data);

};

export const getAccountsByCustomer = (customerId) => {

    return accountAxios.get(`/customer/${customerId}`);

};

export const getAccountById = (accountId) => {

    return accountAxios.get(`/${accountId}`);

};

export const updateAccount = (accountId, data) => {

    return accountAxios.put(`/${accountId}`, data);

};

export const closeAccount = (accountId) => {

    return accountAxios.delete(`/${accountId}`);

};