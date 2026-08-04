import transactionAxios from "../api/transactionAxios";
import transferAxios from "../api/transferAxios";

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

export const transferMoney = (data) => {

    alert("Using Transfer Axios");

    console.log("Transfer URL =", transferAxios.defaults.baseURL);

    return transferAxios.post("/transfer", data);
};