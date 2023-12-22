import React from "react";

import {Container, Nav, Navbar} from "react-bootstrap";
import {NavLink} from "react-router-dom";

const PageNav = () => {
  return (
    <Navbar
      bg=""
      variant="dark"
      expand="lg"
      className="text-center shadow-lg py-4 sticky-top">
      <Container>
        <Navbar.Brand className="me-5">
          <NavLink to="/">Home</NavLink>
        </Navbar.Brand>

        <Navbar.Toggle />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="mx-5">
            <Nav.Link>
              {" "}
              <NavLink to="/Products">Products</NavLink>
            </Nav.Link>

            <Nav.Link>
              <NavLink to="/Parts"> Parts</NavLink>
            </Nav.Link>

            <Nav.Link>
              <NavLink to="/InhousePartForm">Add Inhouse Part</NavLink>
            </Nav.Link>

            <Nav.Link>
              <NavLink to="/OutsourcedPartForm">Add Outsourced Part</NavLink>
            </Nav.Link>

            <Nav.Link>
              <NavLink to="/ProductDetail"> Add Product</NavLink>
            </Nav.Link>

            <Nav.Link>
              <NavLink to="/about"> About</NavLink>
            </Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default PageNav;
