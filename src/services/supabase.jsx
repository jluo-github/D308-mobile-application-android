import { createClient } from "@supabase/supabase-js";

const SUPABASE_URL = "https://wikyriyitnijmkcjytcn.supabase.co";

const supabaseKey = import.meta.env.VITE_SUPABASE_KEY;

const supabase = createClient(SUPABASE_URL, supabaseKey);

export default supabase;
