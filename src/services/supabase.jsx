import { createClient } from "@supabase/supabase-js";

const supabaseUrl = "https://wikyriyitnijmkcjytcn.supabase.co";
// const supabaseKey = process.env.SUPABASE_KEY;
const supabaseKey =
  "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Indpa3lyaXlpdG5pam1rY2p5dGNuIiwicm9sZSI6ImFub24iLCJpYXQiOjE2OTc5MDczMTgsImV4cCI6MjAxMzQ4MzMxOH0.s6wVEQA5xkMiMmdHJtX0pHz5HtRbVY43GzaO6I2VSIw";
const supabase = createClient(supabaseUrl, supabaseKey);

export default supabase;
