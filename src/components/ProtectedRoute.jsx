import {useEffect} from "react";
import {useNavigate} from "react-router-dom";
import {useUser} from "../authentication/useUser";

const ProtectedRoute = ({ children }) => {
  const navigate = useNavigate();

  const { user, isLoading, isAuthenticated } = useUser();

  useEffect(() => {
    if (!isAuthenticated && !isLoading) navigate("/login");
  }, [isAuthenticated, isLoading, navigate]);

  if (isLoading) {
    return <p>Spinner: Loading</p>;
  }

  if (isAuthenticated) return children;
};

export default ProtectedRoute;
