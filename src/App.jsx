import "bootstrap/dist/css/bootstrap.min.css";
import "./custom.scss";
import "./App.css";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import MainScreen from "./myPages/MainScreen";
import InhousePartForm from "./myPages/InhousePartForm";
import OutsourcedPartForm from "./myPages/OutsourcedPartForm";
import ProductDetail from "./myPages/ProductDetail";
import About from "./myPages/About";
import PageNotFound from "./myPages/PageNotFound";
import Parts from "./myPages/Parts";
import Login from "./myPages/Login";
import Products from "./myPages/Products";
import Home from "./components/Home";

import ProtectedRoute from "./components/ProtectedRoute";

import { BrowserRouter, Route, Routes } from "react-router-dom";

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 60 * 1000,
      refetchOnWindowFocus: false,
    },
  },
});

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <ReactQueryDevtools initialIsOpen={false}></ReactQueryDevtools>
      <BrowserRouter>
        <Routes>
          {" "}
          <Route
            element={
              <ProtectedRoute>
                <Home />
              </ProtectedRoute>
            }>
            <Route path="/" exact element={<MainScreen />} />

            <Route path="/parts" element={<Parts />} />
            <Route path="/parts/:id" element={<Parts />} />

            <Route path="/InhousePartForm" element={<InhousePartForm />} />
            <Route path="/InhousePartForm/:id" element={<InhousePartForm />} />
            <Route
              path="/OutsourcedPartForm"
              element={<OutsourcedPartForm />}
            />
            <Route
              path="/OutsourcedPartForm/:id"
              element={<OutsourcedPartForm />}
            />

            <Route path="/products" element={<Products />} />
            <Route path="/products/:id" element={<ProductDetail />} />

            <Route path="/ProductDetail" element={<ProductDetail />} />
            <Route path="/ProductDetail/:id" element={<ProductDetail />} />
            <Route path="/about" element={<About />} />
            {/* <Route path="*" element={<PageNotFound />} /> */}
          </Route>{" "}
          <Route path="/login" element={<Login />} />
          <Route path="*" element={<PageNotFound />} />
        </Routes>
      </BrowserRouter>{" "}
    </QueryClientProvider>
  );
}

export default App;
