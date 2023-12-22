import React, { useEffect, useState } from "react";
import { Table } from "react-bootstrap";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";

const backendUrl =
  "https://v2capstone.victoriousbay-30c8b5e1.westus.azurecontainerapps.io";
// const backendUrl = "http://localhost:8080";

const ProductDetail = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  console.log(id);

  const [product, setProduct] = useState({});
  const [availableParts, setAvailableParts] = useState([]);
  const [associatedParts, setAssociatedParts] = useState([]);
  const [error, setError] = useState("");
  const [priceError, setPriceError] = useState("");

  useEffect(() => {
    if (id) {
      const fetchProducts = async () => {
        try {
          const res = await axios.get(
            `${backendUrl}/api/products/update/${id}`,
            {
              headers: {
                "Access-Control-Allow-Origin": "*",
                "Access-Control-Allow-Methods":
                  "GET,PUT,POST,DELETE,PATCH,OPTIONS",
              },
            }
          );
          const { product, availparts, parts, assparts } = res.data;
          setProduct(product);
          setAvailableParts(availparts);
          setAssociatedParts(assparts);
        } catch (error) {
          if (error.response && error.response.data) {
            const errorMessage = error.response.data[0].defaultMessage;
            setError({ errorMessage });
            console.log("1-Error updating product:", errorMessage);
          } else {
            setError("An error occurred while updating the product.");
            console.error("2-An error occurred while updating the product.");
          }
        }
      };
      fetchProducts();
    } else {
      const fetchProducts = async () => {
        try {
          const res = await axios.get(`${backendUrl}/api/products/add`, {
            headers: {
              "Access-Control-Allow-Origin": "*",
              "Access-Control-Allow-Methods":
                "GET,PUT,POST,DELETE,PATCH,OPTIONS",
            },
          });
          const { product, availparts, parts, assparts } = res.data;
          // setProduct(product);
          setProduct({
            name: "",
            price: "",
            inv: "",
          });
          setAvailableParts(availparts);
          setAssociatedParts(assparts);
        } catch (error) {
          if (error.response && error.response.data) {
            const errorMessage = error.response.data[0].defaultMessage;
            setError({ errorMessage });
            console.log("1-Error updating product:", errorMessage);
          } else {
            setError("An error occurred while updating the product.");
            console.error("2-An error occurred while updating the product.");
          }

          console.log(error);
        }
      };
      fetchProducts();
    }
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setProduct({
      ...product,
      [name]: value,
    });
  };

  const handlePriceChange = (e) => {
    const input = e.target.value;
    const validated = input.match(/^\d+(\.\d{0,2})?$/);
    if (validated || input === "") {
      setProduct({
        ...product,
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
      // const endpoint = id
      //   ? `${backendUrl}/api/products/update/${id}`
      //   : "${backendUrl}/api/products/add";
      const endpoint = `${backendUrl}/api/products/add`;

      const res = await axios.post(endpoint, product, {
        headers: {
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Methods": "GET,PUT,POST,DELETE,PATCH,OPTIONS",
        },
      });
      console.log("Product updated:", res.data);
      navigate("/Products");
    } catch (error) {
      if (error.response && error.response.data) {
        const errorMessage = error.response.data[0].defaultMessage;
        setError({ errorMessage });
        console.log("1-Error updating product:", errorMessage);
      } else {
        setError("An error occurred while updating the product.");
        console.error("2-An error occurred while updating the product.");
      }
    }
  };

  useEffect(() => {
    let timeout = setTimeout(() => {
      setError("");
    }, 3000);
    return () => {
      clearTimeout(timeout);
    };
  }, [error]);

  return (
    <div className=" text-center m-5">
      {/* Product detail form:  */}
      <h1 className="m-5">Product Detail</h1>
      <form className="container" onSubmit={handleSubmit}>
        {/* <input
          type="hidden"
          name="id"
          value={product.id}
          onChange={handleInputChange}
        /> */}

        <input
          className="form-control mb-4 col-4"
          placeholder="Name"
          required
          name="name"
          type="text"
          value={product.name}
          onChange={handleInputChange}
        />

        <input
          className="form-control mb-4 col-4"
          placeholder="Price"
          required
          name="price"
          type="text"
          value={product.price}
          onChange={handlePriceChange}
        />
        <div style={{ color: "red" }}>
          {priceError ? <p>{priceError}</p> : null}
        </div>

        <input
          type={product.id ? "number" : "hidden"}
          name="inv"
          placeholder="Inventory"
          required
          className="form-control mb-4 col-4"
          value={product.inv}
          onChange={handleInputChange}
        />

        <div style={{ color: "red" }}>
          {error.errorMessage ? <p>{error.errorMessage}</p> : null}
        </div>

        <button className="btn btn-primary m-3" type="submit">
          {!product.id ? "Add" : "Update"}
        </button>

        {!product.id && (
          <button
            type="button"
            className="btn btn-primary  m-3"
            onClick={() => {
              navigate(`/`);
            }}>
            Back to Main Menu
          </button>
        )}
      </form>

      {/* Available parts List:  */}

      <div className="container">
        <h2 className="m-5">Available Parts</h2>

        <Table
          className=" darkMode table-dark  align-middle"
          striped
          bordered
          hover>
          <thead className="thead-dark">
            <tr>
              <th>Name</th>
              <th>Price</th>
              <th>Inventory</th>
              <th>Max Inventory</th>
              <th>Min Inventory</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {availableParts.map((availPart) => (
              <tr className="darkMode" key={availPart.id}>
                <td>{availPart.name}</td>
                <td>{availPart.price}</td>
                <td>{availPart.inv}</td>
                <td>{availPart.max}</td>
                <td>{availPart.min}</td>
                <td>
                  {/* add available parts button:  */}
                  <button
                    className="btn btn-primary m-3"
                    onClick={async () => {
                      try {
                        const res = await axios.post(
                          `${backendUrl}/api/products/${id}/associatepart/${availPart.id}`,
                          product,
                          {
                            headers: {
                              "Access-Control-Allow-Origin": "*",
                              "Access-Control-Allow-Methods":
                                "GET,PUT,POST,DELETE,PATCH,OPTIONS",
                            },
                          }
                        );

                        setAssociatedParts([...associatedParts, availPart]);
                        setAvailableParts(
                          availableParts.filter((p) => p.id !== availPart.id)
                        );
                      } catch (error) {
                        if (error.response & error.response.data) {
                          const errorMessage = error.response.data.message;
                          setError({ errorMessage });
                        } else {
                          setError(
                            "Please add and save product before adding parts!"
                          );
                          alert(
                            "Please add and save product before adding parts!"
                          );
                          console.error(
                            "Please save product before adding parts!"
                          );
                        }
                      }
                    }}>
                    Add
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>

        {/* Associated parts List:  */}
        <h2 className="m-5">Associated Parts</h2>
        <Table
          className="darkMode table-dark  align-middle"
          striped
          bordered
          hover>
          <thead className="thead-dark">
            <tr>
              <th>Name</th>
              <th>Price</th>
              <th>Inventory</th>
              <th>Max Inventory</th>
              <th>Min Inventory</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {associatedParts.map((assoPart) => (
              <tr className="darkMode" key={assoPart.id}>
                <td>{assoPart.name}</td>
                <td>{assoPart.price}</td>
                <td>{assoPart.inv}</td>
                <td>{assoPart.max}</td>
                <td>{assoPart.min}</td>
                <td>
                  {/* remove associated parts button:  */}
                  <button
                    className="btn btn-primary m-3"
                    onClick={async () => {
                      try {
                        const res = await axios.post(
                          `${backendUrl}/api/products/${id}/removepart/${assoPart.id}`,
                          product,
                          {
                            headers: {
                              "Access-Control-Allow-Origin": "*",
                              "Access-Control-Allow-Methods":
                                "GET,PUT,POST,DELETE,PATCH,OPTIONS",
                            },
                          }
                        );
                        setAvailableParts([...availableParts, assoPart]);
                        setAssociatedParts(
                          associatedParts.filter((p) => p.id !== assoPart.id)
                        );
                      } catch (error) {
                        console.error("Error remove part:", error);
                      }
                    }}>
                    Remove
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>
    </div>
  );
};

export default ProductDetail;
