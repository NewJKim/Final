# AI Writing Assistant

An AI-powered app that makes your writing better! Choose creative, professional, or academic style.

## Setup

1. **Get API key** from https://dashboard.cohere.com/
2. **Set environment variable:**
   ```bash
   export COHERE_API_KEY="your-key-here"
   ```
3. **Run the app:**
   ```bash
   mvn exec:java
   ```

## Features

- [x] Creative writing mode - Makes text fun and imaginative
- [x] Professional writing mode - Makes text formal for work
- [x] Academic writing mode - Makes text scholarly
- [x] Save/load sessions - Don't lose your work!
- [x] Error handling - Tells you what went wrong

## Design Patterns

- **Strategy:** Different writing modes switch easily
- **Factory:** Creates API requests the right way
- **Observer:** UI knows when text is ready
- **Singleton:** Only one API connection

## Project Structure

```
src/main/java/com/writingassistant/
├── Main.java                  # Start here
├── model/                     # Data and logic
│   ├── strategy/             # Writing modes
│   └── observer/             # UI notifications
├── view/                      # GUI
├── controller/                # Connects model and view
└── service/                   # Talks to API
```

## How to Use

1. Type your text in the top box
2. Pick a mode (Creative/Professional/Academic)
3. Click "Generate"
4. See your improved text!
5. Click "Save" to keep it

## Testing

```bash
mvn test
```

All 10 tests should pass!

## Why Cohere?

- ✅ 100 requests/minute (FREE!)
- ✅ No credit card needed
- ✅ 5,000 free requests/month
- ✅ Works great for this project

## Troubleshooting

**"API key not configured"** → Set COHERE_API_KEY  
**"Cannot connect"** → Check internet  
**"Invalid key"** → Get new key from Cohere dashboard

## Demo (Fianl)
https://drive.google.com/file/d/1SFtq9vSnlTTzM4nz-dAphEFBkdqfrLlo/view?usp=sharing

Shows:
- All 3 writing modes working
- Save and load features
- error handling

## Made By

James Kim  
CS 3560  
15 DEC 2025