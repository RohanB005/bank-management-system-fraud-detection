import Sidebar from "./Sidebar";
import Navbar from "./Navbar";

function MainLayout({ children }) {

    return (

        <>

            <Sidebar />

            <div
                style={{
                    marginLeft: "260px"
                }}
            >

                <Navbar />

                <div className="container-fluid p-4">

                    {children}

                </div>

            </div>

        </>

    );

}

export default MainLayout;