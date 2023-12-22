import React from "react";
import "../custom.scss";
import { Link } from "react-router-dom";

const About = () => {
  return (
    <div className=" m-5 text-center">
      <h1>About</h1>
      <p className=" m-5">
        PurpleCat PC store is a company that manufactures, sells, and services
        custom computers. We offer a wide range of customization options,
        including processor, memory, graphics card, and storage. We also offer
        technical support, maintenance and repair, and installation services. We
        specialize in creating custom computers and accessories, so that our
        customers can create a system that meets their needs and budget.
      </p>
      <footer>
        <Link to="/">
          <button className="btn btn-primary m-5">Back to Main Menu</button>
        </Link>
      </footer>
    </div>
  );
};

export default About;
