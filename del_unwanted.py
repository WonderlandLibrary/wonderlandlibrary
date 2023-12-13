import os

def delete_unwanted_files(folder_path):
    # Initialize counters
    found_count = 0
    deleted_count = 0

    # Walk through the directory tree
    for root, dirs, files in os.walk(folder_path):
        for file in files:
            file_path = os.path.join(root, file)

            # Check if the file matches the criteria
            if file == "Start.java" or file == "pack.png" or file == ".DS_Store" or file == "log4j2.xml":
                print(f"Found: {file_path}")
                found_count += 1

                # Uncomment the line below to actually delete the files
                os.remove(file_path)
                deleted_count += 1

    print(f"\nFound {found_count} files matching the criteria.")
    print(f"Deleted {deleted_count} files.")

if __name__ == "__main__":
    # Get the current script's directory
    script_directory = os.path.dirname(os.path.realpath(__file__))

    # Call the function with the script's directory
    delete_unwanted_files(script_directory)
