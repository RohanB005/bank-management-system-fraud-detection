import MainLayout from "../../components/layout/MainLayout";

function Dashboard() {

    return (

        <MainLayout>

            <h2>

                Dashboard

            </h2>

            <hr />

            <div className="row">

                <div className="col-md-3">

                    <div className="card shadow p-4">

                        <h5>

                            Accounts

                        </h5>

                        <h2>

                            0

                        </h2>

                    </div>

                </div>

                <div className="col-md-3">

                    <div className="card shadow p-4">

                        <h5>

                            Transactions

                        </h5>

                        <h2>

                            0

                        </h2>

                    </div>

                </div>

                <div className="col-md-3">

                    <div className="card shadow p-4">

                        <h5>

                            Transfers

                        </h5>

                        <h2>

                            0

                        </h2>

                    </div>

                </div>

                <div className="col-md-3">

                    <div className="card shadow p-4">

                        <h5>

                            Balance

                        </h5>

                        <h2>

                            ₹0

                        </h2>

                    </div>

                </div>

            </div>

        </MainLayout>

    );

}

export default Dashboard;