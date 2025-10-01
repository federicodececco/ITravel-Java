ITRAVEL

This is the backend for ITRAVEL PWA.
It manage all the APIS needed to interact with the DB, including authentication.
The only part that makes up the backend level of the PWA not present is a small nodejs server used to interact with a Redis and Gemini istance.

Features
-CRUD operation for every resource contained in the DB
-Upload and retrival of images from a Coudflare R2 istance
-JWT based authentication system
-DTO to ease the processing and transfer of data

I consider this project finished, for it was just a way to reconstruct the backend side of the app, previously handled by Supabase
