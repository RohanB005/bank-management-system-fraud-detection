import transactionAxios from "../api/transactionAxios";

export const deposit = (data) => {
    return transactionAxios.post("/deposit", data);
};

export const withdraw = (data) => {
    return transactionAxios.post("/withdraw", data);
};

export const getTransactionHistory = (accountId) => {
    return transactionAxios.get(`/account/${accountId}`);
};

export const getTransactionById = (transactionId) => {
    return transactionAxios.get(`/${transactionId}`);
};