import React from "react";
import {Outlet} from "react-router-dom";
import PageNav from "./PageNav";
import Footer from "./Footer";

const Home = () => {
  return (
    <div>
      <PageNav />
      <Outlet />
      <Footer />
    </div>
  );
};

export default Home;
