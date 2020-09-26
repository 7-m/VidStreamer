Vidstreamer is a YouTube/Vimeo etc. like video streaming platfrom. Current features are basic user authentication, video upload and video streaming.
Built as VTU final year project.

- Bootstrap kubernetes
- install istio
- configure properties DB_HOST, DB_PASSWORD, DB_USER in a file called db-creds and GCS_BUCKET_NAME, GCS_KEY and PROJECT_ID in a file called gcs_creds using kubectl create secret. (Refer service deployment files in the directroy k8s/ for clarity)
- run recreate.sh


Open issues:
- project ID hardcoded across the codebase
- JWT SHA password hardcoded
- bound to GCS platfrom
- SQL FullText search usage in vidindexer might be wrong
- implement auth flow correcly using web filters

Credits-
Big thanks to Shashidhar V for the front end.
