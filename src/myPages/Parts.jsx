import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Table } from "react-bootstrap";
import "../App.css";
import "../custom.scss";
import axios from "axios";

const backendUrl =
  "https://v2capstone.victoriousbay-30c8b5e1.westus.azurecontainerapps.io";
// const backendUrl = "http://localhost:8080";

const Parts = () => {
  const navigate = useNavigate();

  const [partKeyword, setPartKeyword] = useState("");

  const [parts, setParts] = useState([]);

  const [error, setError] = useState("");
  const [errorBuy, setErrorBuy] = useState("");

  const handlePartInputChange = (e) => {
    setPartKeyword(e.target.value);
  };

  const handleInhousePart = () => {
    navigate("/InhousePartForm");
  };

  const handleOutsourcedPart = () => {
    navigate("/OutsourcedPartForm");
  };

  useEffect(() => {
    const fetchParts = async () => {
      try {
        const res = await axios.get(`${backendUrl}/api/parts`);
        setParts(res.data);
      } catch (err) {
        console.log(err);
      }
    };
    fetchParts();
  }, []);

  const searchParts = async (term) => {
    try {
      const res = await axios.get(`${backendUrl}/api/parts`, {
        headers: {
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Methods": "GET,PUT,POST,DELETE,PATCH,OPTIONS",
        },
        params: {
          partKeyword: term,
        },
      });
      setParts(res.data);
    } catch (err) {
      console.log(err);
    }
  };

  const clearSearchParts = async () => {
    try {
      const res = await axios.get(`${backendUrl}/api/parts`);
      setPartKeyword("");
      setParts(res.data);
    } catch (err) {
      console.log(err);
    }
  };

  const generatePartPDF = async (term) => {
    try {
      const res = await axios.get(`${backendUrl}/api/parts/report`, {
        params: {
          partKeyword: term,
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
      {/* Parts Search form:  */}
      <div className="text-center m-5">
        <h2 className="m-5">Parts</h2>
        <form>
          Filter :
          <input
            id="partKeyword"
            name="partKeyword"
            required
            // size="50"
            value={partKeyword}
            onChange={handlePartInputChange}
            type="text"
          />
          &nbsp;
          {/* Search parts button:  */}
          <button
            className="btn btn-primary m-3"
            onClick={(e) => {
              e.preventDefault();
              console.log("partKeyword:", partKeyword);
              searchParts(partKeyword);
            }}>
            Search
          </button>
          &nbsp;
          {/* Clear search parts button:  */}
          <button
            className="btn btn-primary  m-3"
            onClick={(e) => {
              e.preventDefault();
              clearSearchParts();
            }}>
            Clear
          </button>
          {/* Part pdf button */}
          <button
            className="btn btn-primary  m-3"
            onClick={(e) => {
              e.preventDefault();
              generatePartPDF(partKeyword);
            }}>
            PDF Report
          </button>
        </form>

        {/* Add Part button:  */}
        <br />
        <br />
        <button className="btn btn-primary m-3" onClick={handleInhousePart}>
          Add Inhouse Part
        </button>

        <button className="btn btn-primary m-3" onClick={handleOutsourcedPart}>
          Add Outsourced Part
        </button>
      </div>
      {/* Parts Table */}
      <div className="container">
        <Table
          id="parts"
          className=" darkMode table-dark  m-3 text-center align-middle"
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
            {parts.map((part) => (
              <tr className="darkMode" key={part.id}>
                <td>{part.name}</td>
                <td>{part.price}</td>
                <td>{part.inv}</td>
                <td>{part.max}</td>
                <td>{part.min}</td>
                <td>
                  {/* part update button:  */}
                  <button
                    className="btn btn-primary m-3"
                    onClick={() => {
                      if (part.companyName) {
                        if (part.id) {
                          navigate(`/OutsourcedPartForm/${part.id}`);
                        } else {
                          navigate(`/OutsourcedPartForm/`);
                        }
                      } else {
                        navigate(`/InhousePartForm/${part.id}`);
                      }
                    }}>
                    Update
                  </button>

                  {/* part delete button:  */}
                  <button
                    className="btn btn-primary m-3"
                    onClick={async () => {
                      if (
                        window.confirm(
                          "Are you sure you want to delete this part?"
                        )
                      ) {
                        try {
                          const res = await axios.delete(
                            `${backendUrl}/api/parts/delete/${part.id}`
                          );

                          console.log("Part deleted:", res.data);
                          setParts((parts) =>
                            parts.filter((item) => item.id !== part.id)
                          );
                        } catch (error) {
                          if (error.response.data) {
                            setError(error.response.data);
                            console.log(
                              "1-Part cannot be deleted due to existing associations with products!!"
                            );
                          } else {
                            setError("Error deleting the part.");
                            alert(
                              "2-Part cannot be deleted due to existing associations with products!!"
                            );
                          }
                          console.error("Error deleting part:", error);
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

export default Parts;
