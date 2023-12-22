import React, { useEffect, useState } from "react";
import axios from "axios";

import { Link, useNavigate, useParams } from "react-router-dom";

const backendUrl =
  "https://v2capstone.victoriousbay-30c8b5e1.westus.azurecontainerapps.io";
// const backendUrl = "http://localhost:8080";

const OutsourcedPartForm = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  console.log(id);

  const [part, setPart] = useState({});
  const [error, setError] = useState([]);

  const [priceError, setPriceError] = useState("");

  useEffect(() => {
    if (id) {
      const fetchParts = async () => {
        try {
          const res = await axios.get(`${backendUrl}/api/parts/update/${id}`, {
            headers: {
              "Access-Control-Allow-Origin": "*",
              "Access-Control-Allow-Methods":
                "GET,PUT,POST,DELETE,PATCH,OPTIONS",
            },
          });
          setPart(res.data);
        } catch (err) {
          console.log(err);
        }
      };
      fetchParts();
    } else {
      setPart({
        name: "",
        price: "",
        inv: "",
        max: "",
        min: "",
        companyName: "",
      });
    }
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setPart({
      ...part,
      [name]: value,
    });
  };

  const handlePriceChange = (e) => {
    const input = e.target.value;
    const validated = input.match(/^\d+(\.\d{0,2})?$/);
    if (validated || input === "") {
      setPart({
        ...part,
        price: input,
      });
      setPriceError("");
    } else {
      setPriceError("Please enter a valid price!");
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post(
        `${backendUrl}/api/outsourcedParts/add`,
        part,
        {
          headers: {
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "GET,PUT,POST,DELETE,PATCH,OPTIONS",
          },
        }
      );
      console.log("Part added:", res.data);
      setPart(res.data);
      navigate("/Parts");
    } catch (error) {
      if (error.response.data === "Validation failed") {
        setError(
          "Inventory/value must be positive and between or at the Max and Min value!!"
        );
        console.log(
          "1-Inventory must be between or at the Max and Min value!!!"
        );
      } else {
        setError("Error adding the part, please enter valid value.");
        console.log(
          "2-Inventory must be between or at the Max and Min value!!!"
        );
      }
    }
  };

  useEffect(() => {
    let timeout = setTimeout(() => {
      setError("");
    }, 3000);
    return () => clearTimeout(timeout);
  }, [error]);

  return (
    <div className=" text-center m-5">
      <div className="container">
        <h1 className="m-5">Outsourced Part Detail</h1>

        <form onSubmit={handleSubmit}>
          {/* Add hidden form field to handle update */}
          {/* <input type="hidden" name="id" /> */}

          <input
            className="form-control mb-4 col-4"
            placeholder="Name"
            required
            name="name"
            type="text"
            value={part.name}
            onChange={handleInputChange}
          />

          <input
            className="form-control mb-4 col-4"
            placeholder="Price"
            required
            name="price"
            type="text"
            value={part.price}
            onChange={handlePriceChange}
          />
          <div style={{ color: "red" }}>
            {priceError ? <p>{priceError}</p> : null}
          </div>

          <input
            className="form-control mb-4 col-4"
            placeholder="Inventory"
            required
            name="inv"
            type="number"
            value={part.inv}
            onChange={handleInputChange}
          />

          {/* Add max int and min int to the form */}
          <input
            className="form-control mb-4 col-4"
            placeholder="Max Inventory"
            required
            name="max"
            type="number"
            value={part.max}
            onChange={handleInputChange}
          />

          <input
            className="form-control mb-4 col-4"
            placeholder="Min Inventory"
            required
            name="min"
            type="number"
            value={part.min}
            onChange={handleInputChange}
          />

          <input
            className="form-control mb-4 col-4"
            placeholder="Company Name"
            name="companyName"
            type="text"
            value={part.companyName}
            onChange={handleInputChange}
          />

          <button className="btn btn-primary  m-3" type="submit">
            {!part.id ? "Add" : "Update"}
          </button>
        </form>

        <div style={{ color: "red" }}>{error && <p>{error}</p>}</div>

        <footer>
          <Link to="/">
            <button className="btn btn-primary m-5">Back to Main Menu</button>
          </Link>
        </footer>
      </div>
    </div>
  );
};

export default OutsourcedPartForm;
