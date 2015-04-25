function htmlEnco()   
{   
  return str.replace(/&/g, "&amp;")   
            .replace(/</g, "&lt;")   
            .replace(/>/g, "&gt;")  
            .replace(/\'/g, "&#39;")   
            .replace(/\"/g, "&quot;")   
            .replace(/\//g, "&#x2f;"); 
}