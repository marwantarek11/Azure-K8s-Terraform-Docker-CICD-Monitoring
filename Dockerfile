# Use the official Python image from the Docker Hub
FROM python:3.12-slim

# Create a non-root user for security
RUN useradd --create-home --shell /bin/bash appuser

# Set the working directory in the container
WORKDIR /app

# Copy the requirements file into the container
COPY requirements.txt .

# Install the dependencies and clean up cache in one layer
RUN pip install --no-cache-dir -r requirements.txt && rm -rf /root/.cache

# Copy the rest of the application code into the container
COPY . .

# Change ownership of the app directory to the non-root user
RUN chown -R appuser:appuser /app

# Switch to the non-root user
USER appuser

# Expose the port the app runs on
EXPOSE 5000

# Flask environment
ENV FLASK_RUN_HOST=0.0.0.0
ENV FLASK_RUN_PORT=5000
ENV FLASK_ENV=production

# Command to run the application
CMD ["python", "run.py"]
