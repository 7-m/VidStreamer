if [ -z $1 ]
then
	echo "version argument missing	"
	exit 1
fi

echo -e "\n### starting clearn build"
gradlew clean || exit 1
echo -e "\n### building boot jar"
gradlew bootJar || exit 1
echo -e "\n### Done"

echo -e "\n### Building docker image"
sudo docker build -t gcr.io/cloud-test-254814/viduploader:$1 . || exit 1
echo -e "\n### Done"

echo -e "\n### deleting already existing image"
gcloud container images delete gcr.io/cloud-test-254814/viduploader:$1
echo -e "\n### Done"

echo -e "\n### Uploading to gcr"
sudo docker push gcr.io/cloud-test-254814/viduploader:$1 || exit 1
echo -e "\n### Done"
