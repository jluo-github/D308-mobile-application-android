import React, { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Button } from "react-bootstrap";
import "../App.css";
import "../custom.scss";
import Footer from "../components/Footer";
import supabase from "../services/supabase";

import { useLogin } from "../authentication/useLogin";
vfcdx;

const Login = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  console.log(id);

  // const [email, setEmail] = useState("cat1@cat.com");
  // const [password, setPassword] = useState("1234");

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const { login, isLoading } = useLogin();

  const [error, setError] = useState();

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!email || !password) {
      return;
    }
    console.log("clicked");
    login(
      { email, password },
      {
        onSettled: () => {
          setEmail("");
          setPassword("");
        },
      }
    );

    const { data, error } = await supabase.auth.signInWithPassword({
      email,
      password,
    });
    if (error) {
      setError("Wrong username or password!");
      return;
    }
    navigate("/");
    console.log(data);
    return data;
  };

  return (
    <div className=" text-center m-5">
      <h1>PurpleCat PC Store</h1>
      <h1 className="m-5">User Login</h1>

      <form className="container" onSubmit={handleSubmit}>
        <input
          className="form-control mb-4 col-4"
          placeholder="Email"
          required
          name="email"
          type="email"
          autoComplete="username"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          disabled={isLoading}
        />

        <input
          className="form-control mb-4 col-4"
          placeholder="Password"
          required
          name="password"
          type="password"
          autoComplete="current-password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          disabled={isLoading}
        />

        <Button
          className="ps-5 pe-5 m-5 shadow-lg"
          type="submit"
          disabled={isLoading}>
          Login
        </Button>
      </form>

      <div style={{ color: "red" }}>{error && <p>{error}</p>}</div>
      <Footer />
    </div>
  );
};

export default Login;
