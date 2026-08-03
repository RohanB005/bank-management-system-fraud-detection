import axios from "axios";

const authAxios = axios.create({
    baseURL: "http://localhost:9090/api/auth"
});

authAxios.interceptors.request.use(config => {

    const token = localStorage.getItem("token");

    if(token){

        config.headers.Authorization = `Bearer ${token}`;
    }

    return config;

});

export default authAxios;