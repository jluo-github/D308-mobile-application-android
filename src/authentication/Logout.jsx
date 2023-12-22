import React from "react";

import {useLogout} from "./useLogout";
import {Button} from "react-bootstrap";

const Logout = () => {
  const { logout, isLoading } = useLogout();
  return (
    <Button
      className="d-flex ms-auto ps-5 pe-5 mb-5 shadow-lg "
      disabled={isLoading}
      onClick={logout}>
      Logout
    </Button>
  );
};

export default Logout;
