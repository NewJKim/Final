# Project Report

**Name:** James Kim
**Date:** 21 Nov 2025
**Project:** AI Writing Assistant

---

## Challenges I Faced

### Challenge 1: OpenAI Rate Limits
**Problem:** OpenAI's free tier only allows 3 requests per minute. I kept hitting this limit while testing.  
**Solution:** Switched to Cohere API which gives 100 requests/minute for free.  
**Learned:** Always check API limits before starting!

### Challenge 2: API Changes
**Problem:** Cohere changed their API during my project. The old `/generate` endpoint stopped working and gave 404 errors.  
**Solution:** Read their docs and switched to the new `/chat` endpoint. Changed model from `command-r` to `command-a-03-2025`.  
**Learned:** APIs change - always check current documentation!

### Challenge 3: Keeping UI Responsive
**Problem:** When I clicked "Generate," the whole app froze for 3 seconds while waiting for the API.  
**Solution:** Used SwingWorker to run API calls in the background so the UI stays responsive.  
**Learned:** Never do slow tasks on the main UI thread!

---

## Design Pattern Justifications

### Strategy Pattern
**Why I used it:** I needed three different writing styles (Creative, Professional, Academic) that can switch easily.  
**How it works:** Each style is a separate class that implements WritingStrategy interface.  
**Benefit:** Adding a new style is easy - just make a new strategy class!

### Factory Pattern
**Why I used it:** Creating API requests has lots of details (model name, endpoints, JSON format).  
**How it works:** RequestFactory builds the JSON for me so I don't repeat code.  
**Benefit:** When Cohere changed their format, I only had to fix one place!

### Singleton Pattern
**Why I used it:** I only need one API connection for the whole app.  
**How it works:** APIClient.getInstance() always returns the same object.  
**Benefit:** Saves memory and makes sure everyone uses the same settings.

### Observer Pattern
**Why I used it:** The controller needs to know when the API finishes (success or error).  
**How it works:** Controller implements TextGenerationObserver and gets notified.  
**Benefit:** Clean separation - the view doesn't know about API details.

---

## AI Usage (Honest!)

### What I Used AI For:

1. **Debugging JSON Errors**
    - Asked Claude: "Why am I getting 404 error from Cohere API?"
    - Got suggestion to check endpoint URL
    - Found I was using old `/generate` endpoint
    - Fixed: Changed to `/chat` endpoint

2. **Understanding SwingWorker**
    - Asked ChatGPT: "How do I make API calls without freezing GUI?"
    - Got example of SwingWorker
    - Modified: Added my own error handling and Observer pattern
    - Tested: Clicked button many times to make sure it worked

3. **Maven Setup**
    - Used Claude to understand pom.xml structure
    - Asked about adding dependencies
    - Learned: How Maven automatically downloads libraries

### What I Wrote Myself:
- All the design pattern implementations
- The MVC architecture
- All the strategy classes
- The GUI layout
- Error handling logic
- Test cases

### How I Made Sure I Understood:
- Typed all code myself
- Added my own comments explaining what's happening
- Tested everything to make sure it works

---

## Time Spent

**~36 hours**

---