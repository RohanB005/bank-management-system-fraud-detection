import { Link } from "react-router-dom";

function DashboardCard({ title, icon, color, link }) {

    return (

        <div className="col-md-4 mb-4">

            <Link
                to={link}
                className="text-decoration-none"
            >

                <div className={`card border-${color} shadow`}>

                    <div className="card-body text-center">

                        <h1>{icon}</h1>

                        <h5 className="mt-3">

                            {title}

                        </h5>

                    </div>

                </div>

            </Link>

        </div>

    );

}

export default DashboardCard;