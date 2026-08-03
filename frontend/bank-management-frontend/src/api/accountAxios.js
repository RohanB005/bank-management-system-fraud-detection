import axios from "axios";

const accountAxios = axios.create({

    baseURL:"http://localhost:8082/api/accounts"

});

accountAxios.interceptors.request.use(config=>{

    const token=localStorage.getItem("token");

    if(token){

        config.headers.Authorization=`Bearer ${token}`;

    }

    return config;

});

export default accountAxios;