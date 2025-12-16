# System Design Report

**Name:** James Kim  
**Date:** 21 Nov 2025  
**Project:** AI Writing Assistant

---

## System Design

### Architecture Overview

This application follows the **Model-View-Controller (MVC)** architectural pattern with a service layer for API communication.
```
┌─────────────────────────────────────┐
│   View Layer (MainFrame)            │
│   - User interface                   │
│   - Swing components                 │
└──────────────┬──────────────────────┘
               │
               ↓
┌──────────────────────────────────────┐
│   Controller Layer (MainController)  │
│   - Coordinates View ↔ Model         │
│   - Handles user actions             │
│   - Observer pattern implementation  │
└──────────────┬──────────────────────┘
               │
               ↓
┌──────────────────────────────────────┐
│   Service Layer (APIService)         │
│   - API communication                │
│   - Request/response handling        │
└──────────────┬──────────────────────┘
               │
               ↓
┌──────────────────────────────────────┐
│   Model Layer                        │
│   - Strategies (Strategy pattern)    │
│   - APIClient (Singleton pattern)    │
│   - RequestFactory (Factory pattern) │
│   - Domain models                    │
└─────────────────────────────────────┘
```

### Data Flow

1. User types text and selects writing mode in View
2. View triggers Controller's handleGenerate()
3. Controller creates SwingWorker for async processing
4. SwingWorker calls current Strategy's generateText()
5. Strategy uses APIService to call Cohere API
6. APIService uses RequestFactory to build JSON request
7. API response flows back through layers
8. Controller (as Observer) updates View with result

---

## Design Patterns

### 1. Strategy Pattern ✅

**Purpose:** Allow different writing algorithms to be swapped at runtime.

**Implementation:**
- `WritingStrategy` interface defines contract
- `AbstractWritingStrategy` provides base implementation
- `CreativeWritingStrategy`, `ProfessionalWritingStrategy`, `AcademicWritingStrategy` extend base

**Benefit:** Adding new writing modes is easy - just create a new strategy class.

**Code Example:**
```java
WritingStrategy strategy = new CreativeWritingStrategy(apiService);
String result = strategy.generateText(input);
// Can easily swap to:
strategy = new ProfessionalWritingStrategy(apiService);
```

---

### 2. Factory Pattern ✅

**Purpose:** Centralize complex object creation (API request JSON).

**Implementation:**
- `RequestFactory.createCohereRequest()` builds properly formatted JSON for Cohere API

**Benefit:** When API format changed, only had to update one place.

**Code Example:**
```java
JSONObject request = RequestFactory.createCohereRequest(prompt, maxTokens);
// Returns properly formatted Cohere chat request
```

---

### 3. Observer Pattern ✅

**Purpose:** Decouple UI from model - View reacts to generation events.

**Implementation:**
- `TextGenerationObserver` interface
- `MainController` implements observer
- View notifies controller of events
- Controller updates view based on generation state

**Benefit:** View doesn't need to know about API details, just reacts to events.

**Code Example:**
```java
@Override
public void onTextGenerated(String text) {
    view.setOutputText(text);
    view.setGenerateButtonEnabled(true);
}
```

---

### 4. Singleton Pattern ✅

**Purpose:** Ensure only one API client configuration exists.

**Implementation:**
- `APIClient.getInstance()` returns same instance every time
- Private constructor prevents direct instantiation

**Benefit:** Consistent configuration across entire application.

**Code Example:**
```java
APIClient client = APIClient.getInstance();
// Always returns the same instance
```

---

### 5. Template Method Pattern ✅ (NEW!)

**Purpose:** Define algorithm skeleton while letting subclasses customize steps.

**Implementation:**
- `AbstractWritingStrategy.generateText()` defines the flow
- Subclasses implement `getSystemPrompt()` to customize behavior

**Benefit:** Consistent generation flow, customizable prompts.

**Code Example:**
```java
public final String generateText(String input) {
    // Step 1: Validate (same for all)
    if (input == null) return "Error...";
    
    // Step 2: Build prompt (customized by subclass)
    String prompt = getSystemPrompt() + input;
    
    // Step 3: Call API (same for all)
    return apiService.generateText(prompt, 500);
}
```

---

## Four OOP Pillars

### 1. Encapsulation ✅

**Definition:** Hide internal state, expose only necessary interfaces.

**Implementation:**
```java
public class APIClient {
    private String apiKey;           // Hidden
    private String apiEndpoint;      // Hidden
    
    public String getApiKey() {      // Controlled access
        return apiKey;
    }
    
    public boolean isConfigured() {  // Validated access
        return apiKey != null && !apiKey.isEmpty();
    }
}
```

**Benefits:**
- Internal state protected
- Can change implementation without breaking clients
- Validation ensures data integrity

---

### 2. Inheritance ✅

**Definition:** Create new classes based on existing ones.

**Implementation:**
```java
// Parent class
public abstract class AbstractWritingStrategy implements WritingStrategy {
    protected final APIService apiService;
    
    // Common implementation inherited by all children
    public final String generateText(String input) { ... }
}

// Child class
public class CreativeWritingStrategy extends AbstractWritingStrategy {
    // Inherits generateText() from parent
    // Provides its own getSystemPrompt()
}
```

**Benefits:**
- Code reuse (generateText logic shared)
- Consistent behavior across all strategies
- Easy to add new strategies

---

### 3. Polymorphism ✅

**Definition:** Same interface, different implementations.

**Implementation:**

**Runtime polymorphism:**
```java
WritingStrategy strategy;  // Interface type
strategy = new CreativeWritingStrategy(service);
strategy.generateText(input);  // Calls Creative version

strategy = new AcademicWritingStrategy(service);
strategy.generateText(input);  // Calls Academic version
```

**Method overriding:**
```java
public class GenerationRequest {
    @Override
    public boolean equals(Object o) { ... }  // Custom equality
    
    @Override
    public int hashCode() { ... }  // Custom hashing
    
    @Override
    public String toString() { ... }  // Custom string representation
}
```

**Benefits:**
- Flexibility to swap implementations
- Cleaner code (no if/else chains)
- Easier testing (can mock interfaces)

---

### 4. Abstraction ✅

**Definition:** Hide complex implementation details, show only essentials.

**Implementation:**

**Interface abstraction:**
```java
public interface WritingStrategy {
    String generateText(String input);
    // Clients don't see: API calls, JSON parsing, error handling
}
```

**Abstract class abstraction:**
```java
public abstract class AbstractWritingStrategy {
    // Hides: validation, prompt building, API calling
    // Shows: simple generateText() method
    
    protected abstract String getSystemPrompt();
    // Subclasses only need to define their prompt
}
```

**Benefits:**
- Simplified interfaces for clients
- Implementation can change without affecting users
- Easier to understand and maintain

---

## Design Tradeoffs

### Cohere vs OpenAI

**Decision:** Use Cohere API instead of OpenAI

**Reasoning:**
- OpenAI free tier: 3 requests/minute (too slow for testing)
- Cohere free tier: 100 requests/minute
- Both provide quality text generation

**Tradeoff:** Had to learn new API when Cohere updated endpoints, but faster testing was worth it.

---

### SwingWorker vs Threading

**Decision:** Use SwingWorker for async operations

**Reasoning:**
- Built-in support for UI updates
- Easier error handling than raw threads
- Prevents common threading bugs

**Tradeoff:** More Java-specific (not portable to other UI frameworks), but safer and simpler.

---

### Environment Variables vs Config File

**Decision:** Support both environment variables AND config.properties

**Reasoning:**
- Environment variables: good for deployment
- Config file: good for development
- Fallback increases flexibility

**Tradeoff:** More configuration code, but better user experience.

---

### Simple Error Handling vs Retry Logic

**Decision:** Basic error handling (no retry/backoff in current version)

**Reasoning:**
- Time constraint
- Cohere's free tier is reliable
- Simple rate limiting (1 second delay) works

**Tradeoff:** Less resilient to network issues, but simpler implementation. Could add retry logic in future version.

---

## Challenges Faced

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

### Challenge 4: Demonstrating Inheritance
**Problem:** Initially had all strategies directly implement the interface, which didn't clearly show inheritance.
**Solution:** Created AbstractWritingStrategy base class that all strategies extend.
**Learned:** Inheritance requires actual class hierarchy (parent→child), not just interface implementation.

---

## What I Learned

### Technical Skills
1. **MVC Architecture** - How to properly separate concerns in a GUI application
2. **Design Patterns** - When and why to use each pattern
3. **Async Programming** - SwingWorker for non-blocking UI
4. **API Integration** - Handling network calls, errors, timeouts
5. **Testing** - JUnit for validating patterns and error handling

### Software Engineering Principles
1. **Abstraction** - Hide complexity, expose simple interfaces
2. **DRY Principle** - Use inheritance to avoid code duplication
3. **Error Handling** - Always validate input, handle edge cases
4. **Documentation** - Clear docs make maintenance easier

### Professional Practices
1. **Never commit API keys** - Use environment variables or config files
2. **Test error paths** - Don't just test happy path
3. **User experience matters** - Async operations, status messages, error dialogs
4. **Plan for change** - APIs evolve, use abstraction layers

---

## AI Usage Disclosure

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

4. **Understanding Design Patterns**
   - Asked about Template Method pattern implementation
   - Got examples of abstract base classes
   - Implemented: Created AbstractWritingStrategy
   - Customized: Added my own validation and error handling

### What I Wrote Myself:
- All the design pattern implementations
- The MVC architecture structure
- All the strategy classes
- The GUI layout and interactions
- Error handling logic
- Test cases
- This documentation

### How I Ensured Understanding:
- Typed all code myself (no copy-paste)
- Added my own comments explaining logic
- Tested every feature multiple times
- Modified AI suggestions to fit my needs
- Could explain any line of code if asked

### Percentage Breakdown:
- **Conceptual help from AI:** ~25% (pattern explanations, examples)
- **Code I wrote:** ~75% (all implementation, testing, debugging)
- **Understanding:** 100% (I can explain every part)

---

## Time Spent

**Total: ~40 hours**

- Initial setup & API research: 4 hours
- Core implementation (MVC, patterns): 15 hours
- GUI development: 8 hours
- Testing & debugging: 6 hours
- Documentation: 4 hours
- Refactoring (adding inheritance): 3 hours

---

## Conclusion

This project successfully demonstrates:
- ✅ Complete MVC architecture
- ✅ 5 design patterns (Strategy, Factory, Observer, Singleton, Template Method)
- ✅ All 4 OOP pillars with clear examples
- ✅ Real API integration with async processing
- ✅ Comprehensive testing
- ✅ Professional documentation

The application is functional, well-structured, and demonstrates software engineering best practices.