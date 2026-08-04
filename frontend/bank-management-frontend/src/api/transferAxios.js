import axios from "axios";

const transferAxios = axios.create({
    baseURL: "http://localhost:8083/api/transactions"
});

console.log("Transfer Base URL:", transferAxios.defaults.baseURL);

export default transferAxios;