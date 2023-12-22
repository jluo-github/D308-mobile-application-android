import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import "./custom.scss"; // Import custom SASS file
import App from "./App";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
