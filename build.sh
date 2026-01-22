#!/bin/bash
# Build script pro CZSK Tier Tagger mod

echo "🔨 Building CZSK Tier Tagger mod..."

cd "$(dirname "$0")"

# Clean and build
gradle clean build --no-daemon

# Check if build was successful
if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi

# Copy dev jar as final (workaround for remapJar issue)
cp build/devlibs/czsk-tier-tagger-*-dev.jar build/libs/czsk-tier-tagger-1.0.0.jar

echo ""
echo "✅ Build successful!"
echo ""
echo "📦 Output file: build/libs/czsk-tier-tagger-1.0.0.jar"
echo "📊 Size: $(ls -lh build/libs/czsk-tier-tagger-1.0.0.jar | awk '{print $5}')"
echo ""
echo "📋 To install:"
echo "   1. Copy build/libs/czsk-tier-tagger-1.0.0.jar to .minecraft/mods/"
echo "   2. Make sure you have Fabric Loader and Fabric API installed"
echo "   3. Launch Minecraft 1.21.4"
