import { createClient } from "@supabase/supabase-js";
import process from "process";

const supabaseUrl = "https://wikyriyitnijmkcjytcn.supabase.co";
const supabaseKey = process.env.SUPABASE_KEY;

const supabase = createClient(supabaseUrl, supabaseKey);

export default supabase;
