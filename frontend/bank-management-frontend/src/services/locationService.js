export const getTransactionCity = async () => {

    try {

        const response = await fetch("https://ipapi.co/json/");

        const data = await response.json();

        return data.city;

    }

    catch (error) {

        console.log(error);

        return "Unknown";

    }

};