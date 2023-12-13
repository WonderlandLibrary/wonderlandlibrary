import os

def delete_unwanted_files(folder_path):
    # Initialize counters
    found_count = 0
    deleted_count = 0

    # Undesired directory names
    undesired_dirs = ["javax", "shadersmod", "viamcp", "META-INF", "google", "joptsimple", "tv", "oshi", "ibm", "sun", "iaversion", "mojang", "jcraft", "jhlabs"]

    # Walk through the directory tree
    for root, dirs, files in os.walk(folder_path):
        for file in files:
            file_path = os.path.join(root, file)

            # Check if the file matches the criteria
            if file in ["Start.java", "pack.png", ".DS_Store", "log4j2.xml", "InjectionAPI.java"]:
                print(f"Found file: {file_path}")
                found_count += 1

                # Ask for user confirmation before deletion
                user_input = input("Do you want to delete this file? (y/n): ").lower()
                if user_input == "y":
                    os.remove(file_path)
                    deleted_count += 1
                    print(f"Deleted file: {file_path}")
                else:
                    print(f"Skipped file: {file_path}")

        for dir_name in dirs:
            dir_path = os.path.join(root, dir_name)

            # Check if the entire directory name is in the undesired directory names
            if dir_name in undesired_dirs:
                print(f"Found directory: {dir_path}")
                found_count += 1

                # Ask for user confirmation before deletion
                user_input = input("Do you want to delete this directory and its contents? (y/n): ").lower()
                if user_input == "y":
                    for root_dir, _, files in os.walk(dir_path, topdown=False):
                        for file_name in files:
                            file_path = os.path.join(root_dir, file_name)
                            os.remove(file_path)
                            print(f"Deleted file: {file_path}")
                        os.rmdir(root_dir)
                        print(f"Deleted directory: {root_dir}")
                    deleted_count += 1
                else:
                    print(f"Skipped directory: {dir_path}")

    print(f"\nFound {found_count} files and directories matching the criteria.")
    print(f"Deleted {deleted_count} files and directories.")

if __name__ == "__main__":
    # Get the current script's directory
    script_directory = os.path.dirname(os.path.realpath(__file__))

    # Call the function with the script's directory
    delete_unwanted_files(script_directory)
