const fs = require('fs');
const path = require('path');

const wildflyDirectory = path.join('/home/iughita/Software/EAM-wildfly-22.0.0.Final');

const mode = process.argv[2];
if (!mode) {
  console.error('❌ Please specify the mode');
  process.exit(1);
}

const sourceFile = 'standalone-' + mode +'.xml';

const standalonePath = path.join(wildflyDirectory, 'standalone', 'configuration', 'standalone.xml');
const sourcePath = path.join(wildflyDirectory, 'standalone', 'configuration', sourceFile);

try {
  const sourceContent = fs.readFileSync(sourcePath, 'utf-8');
  fs.writeFileSync(standalonePath, sourceContent, 'utf-8');
  console.log(`✅ Replaced standalone.xml with ${sourceFile} content`);
} catch (error) {
  console.error(`❌ Error replacing XML files: ${error.message}`);
}
