import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Table } from "react-bootstrap";
import "../App.css";
import "../custom.scss";
import axios from "axios";

const backendUrl =
  "https://v2capstone.victoriousbay-30c8b5e1.westus.azurecontainerapps.io";
// const backendUrl = "http://localhost:8080";

const Products = () => {
  const navigate = useNavigate();

  const [productKeyword, setProductKeyword] = useState("");
  const [products, setProducts] = useState([]);
  const [error, setError] = useState("");
  const [errorBuy, setErrorBuy] = useState("");

  const handleProductInputChange = (e) => {
    setProductKeyword(e.target.value);
  };

  const handleAddProduct = () => {
    navigate("/ProductDetail");
  };

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const res = await axios.get(`${backendUrl}/api/products`);
        setProducts(res.data);
      } catch (err) {
        console.log(err);
      }
    };
    fetchProducts();
  }, []);

  const searchProducts = async (term) => {
    try {
      const res = await axios.get(`${backendUrl}/api/products`, {
        headers: {
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Methods": "GET,PUT,POST,DELETE,PATCH,OPTIONS",
        },
        params: {
          productKeyword: term,
        },
      });
      setProducts(res.data);
    } catch (err) {
      console.log(err);
    }
  };

  const clearSearchProducts = async () => {
    try {
      const res = await axios.get(`${backendUrl}/api/products`);
      setProductKeyword("");
      setProducts(res.data);
    } catch (err) {
      console.log(err);
    }
  };

  const generateProductPDF = async (term) => {
    try {
      const res = await axios.get(`${backendUrl}/api/products/report`, {
        params: {
          productKeyword: term,
        },
        responseType: "blob",
      });

      // Create a blob from the response data
      const blob = new Blob([res.data], { type: "application/pdf" });
      // Create a URL for the blob
      const pdfUrl = window.URL.createObjectURL(blob);
      // Open the PDF in a new tab or window
      window.open(pdfUrl, "_blank");
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    let timeout = setTimeout(() => {
      setError("");
      setErrorBuy("");
    }, 3000);
    return () => clearTimeout(timeout);
  }, [error, errorBuy]);

  return (
    <div className=" text-center m-5">
      <div className="text-center m-5">
        <h2 className="m-5">Products</h2>
        <form>
          Filter:
          <input
            id="productKeyword"
            name="productKeyword"
            required
            // size="50"
            value={productKeyword}
            onChange={handleProductInputChange}
            type="text"
          />
          &nbsp;
          {/* Search products button: */}
          <button
            className="btn btn-primary  m-3"
            onClick={(e) => {
              e.preventDefault();
              console.log("partKeyword:", productKeyword);
              searchProducts(productKeyword);
            }}>
            Search
          </button>
          &nbsp;
          {/* Clear search products button: */}
          <button
            className="btn btn-primary m-3"
            onClick={(e) => {
              e.preventDefault();
              clearSearchProducts();
            }}>
            Clear
          </button>
          {/* Product pdf button */}
          <button
            className="btn btn-primary  m-3"
            onClick={(e) => {
              e.preventDefault();
              generateProductPDF(productKeyword);
            }}>
            PDF Report
          </button>
        </form>
        <br />
        <br />
        <button className="btn btn-primary m-3" onClick={handleAddProduct}>
          Add Product
        </button>
      </div>
      {/* Products Table */}
      <div className="container">
        <Table
          id="products"
          // className="table table-bordered table-striped m-3 text-center align-middle"
          className="darkMode table-dark m-3 text-center align-middle"
          striped
          bordered
          hover>
          <thead className="thead-dark">
            <tr>
              <th>Name</th>
              <th>Price</th>
              <th>Inventory</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {products.map((product) => (
              <tr className="darkMode" key={product.id}>
                <td>{product.name}</td>
                <td>{product.price}</td>
                <td>{product.inv}</td>
                <td>
                  {/* Products Update button */}
                  <button
                    className="btn btn-primary m-3"
                    onClick={() => {
                      navigate(`/ProductDetail/${product.id}`);
                    }}>
                    Update
                  </button>

                  {/* Products Buy button */}
                  <button
                    className="btn btn-primary m-3"
                    onClick={async () => {
                      try {
                        const res = await axios.post(
                          `${backendUrl}/api/products/buy/${product.id}`,
                          {
                            headers: {
                              "Access-Control-Allow-Origin": "*",
                              "Access-Control-Allow-Methods":
                                "GET,PUT,POST,DELETE,PATCH,OPTIONS",
                            },
                          }
                        );
                        const updatedProducts = [...products];
                        const productIndex = updatedProducts.findIndex(
                          (p) => p.id === product.id
                        );

                        if (productIndex !== -1) {
                          updatedProducts[productIndex] = {
                            ...updatedProducts[productIndex],
                            inv: updatedProducts[productIndex].inv - 1,
                          };
                          setProducts(updatedProducts);
                        }
                        console.log("Product bought:", res.data);
                        alert("Product bought successfully");
                        navigate("/products");
                      } catch (error) {
                        setErrorBuy(
                          "Purchase unsuccessful, product is out of stock!"
                        );
                      }
                    }}>
                    Buy Now
                  </button>

                  {/* Products Delete button */}
                  <button
                    className="btn btn-primary  m-3"
                    onClick={async () => {
                      if (
                        window.confirm(
                          "Are you sure you want to delete this product?"
                        )
                      ) {
                        try {
                          const res = await axios.delete(
                            `${backendUrl}/api/products/delete/${product.id}`
                          );
                          console.log("Product deleted:", res.data);
                          setProducts((products) =>
                            products.filter((item) => item.id !== product.id)
                          );
                        } catch (error) {
                          console.error("Error deleting product:", error);
                        }
                      }
                    }}>
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>{" "}
        <div style={{ color: "red" }}>{errorBuy && <p>{errorBuy}</p>}</div>
        <footer>
          <Link to="/">
            <button className="btn btn-primary  m-5">Back to Main Menu</button>
          </Link>
        </footer>
      </div>
    </div>
  );
};

export default Products;
